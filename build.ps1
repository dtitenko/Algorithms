Push-Location .\test-data
Get-ChildItem -Filter *.zip | ForEach-Object { Expand-Archive $_.Name -DestinationPath .\ }
Pop-Location