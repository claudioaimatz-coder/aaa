#!/usr/bin/env python3
"""Troca ids SRG (m_123_, f_456_) por nomes Mojang em todos os .java de uma pasta.

Uso: apply_names.py <names.json> <pasta_origem> <pasta_destino> <relatorio.txt>
"""
import json
import re
import shutil
import sys
from collections import Counter
from pathlib import Path

names = json.load(open(sys.argv[1], encoding="utf-8"))
src, dst, report = Path(sys.argv[2]), Path(sys.argv[3]), Path(sys.argv[4])
pat = re.compile(r"\b[mf]_\d+_\b")

missing = Counter()
replaced = 0
files = 0
for f in src.rglob("*"):
    if not f.is_file():
        continue
    rel = f.relative_to(src)
    out = dst / rel
    out.parent.mkdir(parents=True, exist_ok=True)
    if f.suffix != ".java":
        shutil.copy2(f, out)
        continue
    files += 1
    txt = f.read_text(encoding="utf-8", errors="replace")

    def sub(m):
        global replaced
        k = m.group(0)
        if k in names:
            replaced += 1
            return names[k]
        missing[k] += 1
        return k

    out.write_text(pat.sub(sub, txt), encoding="utf-8")

lines = [f"arquivos .java: {files}",
         f"ids substituidos: {replaced}",
         f"ids sem nome (ficaram como estavam): {len(missing)} ({sum(missing.values())} ocorrencias)", ""]
lines += [f"{k} x{v}" for k, v in missing.most_common()]
report.write_text("\n".join(lines) + "\n", encoding="utf-8")
print("\n".join(lines[:3]))
