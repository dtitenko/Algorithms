Push-Location .\libs
jar xf stdlib.jar
jar xf algs4.jar
Pop-Location

Push-Location .\test-data
Get-ChildItem -Filter *.zip | ForEach-Object { Expand-Archive $_.Name -DestinationPath .\ }
Pop-Location