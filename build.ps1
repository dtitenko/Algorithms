Push-Location .\test-data
Get-ChildItem * -Exclude *.zip -Recurse | Remove-Item -Recurse
Get-ChildItem -Filter *.zip | ForEach-Object { Expand-Archive $_.Name -DestinationPath .\ }
Pop-Location