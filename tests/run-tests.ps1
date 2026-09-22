$ErrorActionPreference = 'Stop'
$raizProjeto = Split-Path -Parent $PSScriptRoot
Push-Location $raizProjeto
try {
    New-Item -ItemType Directory -Path build -Force | Out-Null
    $fontes = @(Get-ChildItem -LiteralPath src -Filter '*.java' | ForEach-Object { $_.FullName })
    & javac -encoding UTF-8 --release 17 -Xlint:all -Werror -d build @fontes tests/ModeloTest.java
    if ($LASTEXITCODE -ne 0) { throw 'Falha na compilacao.' }
    & java -cp build ModeloTest
    if ($LASTEXITCODE -ne 0) { throw 'Falha nos testes.' }
} finally {
    Pop-Location
}
