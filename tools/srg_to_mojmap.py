#!/usr/bin/env python3
"""Gera um mapa id SRG -> nome Mojang (m_123_ -> getItemInHand, f_456_ -> random).

Uso: srg_to_mojmap.py <joined.tsrg> <client.txt> <saida.json>

  joined.tsrg : MCPConfig (TSRG2, obf -> srg)  - membros identificados por nome ofuscado + descritor ofuscado
  client.txt  : Mojang mappings (ProGuard, mojang -> obf)

A juncao e feita pelo nome ofuscado: (classe ofuscada, nome ofuscado, descritor ofuscado).
"""
import json
import re
import sys
from collections import defaultdict

PRIM = {"int": "I", "boolean": "Z", "byte": "B", "char": "C", "short": "S",
        "long": "J", "float": "F", "double": "D", "void": "V"}


def parse_mojmap(path):
    """Retorna (cls_to_obf, classes) onde classes[obf_cls] = {'fields': {obf: name}, 'methods': [(obf, mojang_name, ret, [args])]}"""
    cls_to_obf = {}
    raw = []                      # (obf_cls, kind, data) - descritores so podem ser montados depois
    cur = None
    for line in open(path, encoding="utf-8"):
        if line.startswith("#") or not line.strip():
            continue
        if not line.startswith(" "):
            m = re.match(r"^(\S+) -> (\S+):$", line.strip())
            if m:
                cls_to_obf[m.group(1)] = m.group(2)
                cur = m.group(2)
            continue
        body = line.strip()
        m = re.match(r"^(?:\d+:\d+:)?(\S+) (\S+?)\((.*?)\)(?::\d+:\d+)? -> (\S+)$", body)
        if m:                                           # metodo
            ret, name, args, obf = m.groups()
            raw.append((cur, "m", (obf, name, ret, [a for a in args.split(",") if a])))
            continue
        m = re.match(r"^(\S+) (\S+) -> (\S+)$", body)   # campo
        if m:
            raw.append((cur, "f", (m.group(3), m.group(2))))
    return cls_to_obf, raw


def obf_type(t, cls_to_obf):
    dims = 0
    while t.endswith("[]"):
        dims += 1
        t = t[:-2]
    if t in PRIM:
        d = PRIM[t]
    else:
        d = "L" + cls_to_obf.get(t, t).replace(".", "/") + ";"
    return "[" * dims + d


def build(tsrg_path, mojmap_path):
    cls_to_obf, raw = parse_mojmap(mojmap_path)
    # obf_cls -> {obf_field: name} e obf_cls -> {(obf_method, obf_desc): name}
    fields = defaultdict(dict)
    methods = defaultdict(dict)
    for cls, kind, d in raw:
        if kind == "f":
            fields[cls][d[0]] = d[1]
        else:
            obf, name, ret, args = d
            desc = "(" + "".join(obf_type(a, cls_to_obf) for a in args) + ")" + obf_type(ret, cls_to_obf)
            methods[cls][(obf, desc)] = name

    names, conflicts = {}, []
    unmatched = 0
    cur = None
    for line in open(tsrg_path, encoding="utf-8"):
        line = line.rstrip("\n")
        if not line or line.startswith("tsrg2"):
            continue
        if not line.startswith("\t"):
            cur = line.split(" ")[0]
            continue
        if line.startswith("\t\t"):
            continue                                    # parametros
        parts = line.strip().split(" ")
        if len(parts) == 3:                             # campo: obf srg id
            obf, srg, _ = parts
            name = fields.get(cur, {}).get(obf)
        elif len(parts) == 4:                           # metodo: obf desc srg id
            obf, desc, srg, _ = parts
            name = methods.get(cur, {}).get((obf, desc))
        else:
            continue
        if not re.fullmatch(r"[mf]_\d+_", srg):
            continue
        if name is None:
            unmatched += 1
            continue
        if srg in names and names[srg] != name:
            conflicts.append((srg, names[srg], name))
            continue
        names[srg] = name
    return names, conflicts, unmatched


if __name__ == "__main__":
    names, conflicts, unmatched = build(sys.argv[1], sys.argv[2])
    with open(sys.argv[3], "w", encoding="utf-8") as fh:
        json.dump(names, fh, indent=0, sort_keys=True)
    print(f"ids mapeados: {len(names)}  sem correspondencia no mojmap: {unmatched}  conflitos: {len(conflicts)}")
    for c in conflicts[:20]:
        print("  conflito", c)
    if len(names) < 10000:
        sys.exit("poucos ids mapeados: formato de entrada inesperado")
