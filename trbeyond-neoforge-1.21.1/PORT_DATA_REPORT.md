# Relatorio da conversao de assets/data

## revisar_codec (1)

- `/home/claude/aaa/trbeyond-neoforge-1.21.1/src/main/resources/data/trbeyond/worldgen/structure/colosseum.json`: size=30: o codec de jigsaw vanilla limita a 20; confirmar no codec da estrutura custom trbeyond:colosseum

## tag_movida_para_namespace_do_addon (11)

- `data/forge/tags/blocks/dungeon_placeable.json`: forge:dungeon_placeable -> trbeyond:dungeon_placeable (nao existe no Tensura 2.0; o codigo deve usar o novo namespace)
- `data/forge/tags/entity_types/dungeon_boss.json`: forge:dungeon_boss -> trbeyond:dungeon_boss (nao existe no Tensura 2.0; o codigo deve usar o novo namespace)
- `data/forge/tags/entity_types/loot_t1.json`: forge:loot_t1 -> trbeyond:loot_t1 (nao existe no Tensura 2.0; o codigo deve usar o novo namespace)
- `data/forge/tags/entity_types/loot_t2.json`: forge:loot_t2 -> trbeyond:loot_t2 (nao existe no Tensura 2.0; o codigo deve usar o novo namespace)
- `data/forge/tags/entity_types/loot_t3.json`: forge:loot_t3 -> trbeyond:loot_t3 (nao existe no Tensura 2.0; o codigo deve usar o novo namespace)
- `data/forge/tags/entity_types/loot_t4.json`: forge:loot_t4 -> trbeyond:loot_t4 (nao existe no Tensura 2.0; o codigo deve usar o novo namespace)
- `data/forge/tags/entity_types/loot_t5.json`: forge:loot_t5 -> trbeyond:loot_t5 (nao existe no Tensura 2.0; o codigo deve usar o novo namespace)
- `data/forge/tags/entity_types/loot_t6.json`: forge:loot_t6 -> trbeyond:loot_t6 (nao existe no Tensura 2.0; o codigo deve usar o novo namespace)
- `data/forge/tags/entity_types/loot_t7.json`: forge:loot_t7 -> trbeyond:loot_t7 (nao existe no Tensura 2.0; o codigo deve usar o novo namespace)
- `data/forge/tags/entity_types/not_elite.json`: forge:not_elite -> trbeyond:not_elite (nao existe no Tensura 2.0; o codigo deve usar o novo namespace)
- `data/forge/tags/entity_types/slime_dolls.json`: forge:slime_dolls -> trbeyond:slime_dolls (nao existe no Tensura 2.0; o codigo deve usar o novo namespace)

## nao_portado (1)

- `data/trbeyond/loot_modifiers/entities/dungeon_entities.json`: loot modifier declarado como inativo pelo autor; omitido

## substituido (1)

- `/home/claude/aaa/trbeyond-neoforge-1.21.1/src/main/resources/data/trbeyond/advancement/lifes_not_fair.json`: tensura:unlit_lantern nao existe no Tensura 2.0; usado minecraft:lantern

