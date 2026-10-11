# Tensura Dungeon (trbeyond): port 1.19.2 Forge → 1.21.1 NeoForge

Base: `trbeyond-1.0.1.4.jar` (1.19.2, Forge). Alvo: NeoForge 1.21.1 com **Tensura 2.0.x** (a versão 1.21.1 do Tensura é uma reescrita com ManasCore e Architectury, não um port do 1.x).

## Como compilar

1. JDK 21.
2. Coloque em `libs/` os jars listados em `libs/README.md` (Tensura 2.0.x e dependências).
3. Abra a pasta no IntelliJ (ele baixa o Gradle sozinho) ou rode `gradle wrapper` e depois `./gradlew build`.
4. Se `neo_version` em `gradle.properties` não existir, troque pela mais recente da série 21.1.x.

## Estado

| Parte | Estado |
|---|---|
| Build (`build.gradle`, `settings.gradle`, `gradle.properties`) | pronto, **não testado** (o ambiente de trabalho não alcança os repositórios Maven) |
| `neoforge.mods.toml`, `pack.mcmeta` | pronto |
| Assets (texturas, modelos, geo, animações, sons, lang) | copiados sem alteração; o formato já é compatível |
| Data (advancement, loot_table, recipe, structure, worldgen, tags, dados do Tensura) | convertido e validado (213 JSONs válidos; todos os ids `tensura:*` conferidos contra o lang do Tensura 2.0) |
| Código Java | **pendente** |
| Mixins (`trbeyond.mixins.json` está vazio de propósito) | **pendente** |

O relatório da conversão de data está em `PORT_DATA_REPORT.md`.

## O que mudou nos dados

- Pastas no singular: `advancements→advancement`, `loot_tables→loot_table`, `recipes→recipe`, `structures→structure`.
- Advancements: `icon.item→icon.id`; `player_killed_entity` agora usa predicados de loot (`entity_properties`); `location.structure→structures`. O filtro de dimensão de `no_mercy` era ignorado no 1.19.2 e agora é aplicado de fato.
- Loot tables: `enchant_randomly.enchantments→options`.
- Receita: `tensura:smithing` → `tensura:smithing_bench` (`input1/2`, `inputAmount1/2`, `output`).
- Dados custom do Tensura 2.0:
  - `entity/ep` → `entity_existence` (`entity→type`, `skills→abilities`, `minEP/maxEP→min_magicule/max_magicule`)
  - `gear/ep` → `gear_existence` (`item→type`, `evolvingItem→evolution`, `gainEP→epGain`)
  - `tensura/magicule/modifier/level` → `level_magicule`
- Tags: as do namespace `forge` que o Tensura 1.x lia agora estendem as tags `tensura:*` equivalentes. As que não existem no Tensura 2.0 (`loot_t1..7`, `not_elite`, `slime_dolls`, `dungeon_boss`, `dungeon_placeable`) foram para o namespace `trbeyond`; **o código deve ler `trbeyond:<tag>`**.
- Ids renomeados no Tensura 2.0: `bulldeer→cattledeer`, `armoursaurus→armorsaurus`. `tensura:unlit_lantern` (ícone de advancement) não existe mais; usei `minecraft:lantern`.
- Não portado: `loot_modifiers/entities/dungeon_entities.json` (o próprio autor marcou como inativo).

## Pontos a conferir no código

- `worldgen/structure/colosseum.json` tem `size: 30`; o jigsaw vanilla limita a 20. Depende do codec da estrutura custom `trbeyond:colosseum`.
- As estruturas `.nbt` são do DataVersion 1.19.2; o jogo as atualiza ao carregar.
