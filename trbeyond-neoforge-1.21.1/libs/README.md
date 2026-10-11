# libs/

Coloque aqui os `.jar` das dependências para 1.21.1 / NeoForge. O build compila contra todos os jars desta pasta.

Necessários (versões que o Tensura 2.0.1.3 declara):

- `tensura` 2.0.1.3 (ou mais novo)
- `manascore_storage`, `manascore_skill`, `manascore_race` (>= 3.0.2.9)
- `architectury` (>= 13.0.8)
- `geckolib` (>= 4.7.5)
- `smartbrainlib` (>= 1.16.7)
- `terrablender`
- `playeranimator` (player-animation-lib) para 1.21.1 NeoForge

Para testar com `./gradlew runClient`, copie os mesmos jars para `run/mods/`.
Os `.jar` ficam fora do Git (`.gitignore`).
