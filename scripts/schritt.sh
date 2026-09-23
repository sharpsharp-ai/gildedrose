#!/bin/bash
# Ein Refactoring-Schritt. Läuft mvn -q verify. Grün: Commit von src/main mit der Botschaft. Rot: src/main zurück auf den
# letzten Commit. Nimmt nur Schritte, die ausschließlich src/main ändern und höchstens 40 geänderte Zeilen haben.
# Aufruf: scripts/schritt.sh "<Refactoring>: <was>"      z. B. scripts/schritt.sh "Extract Method: Preis je Position"
set -u
max=40
msg=${1:-}
[ -n "$msg" ] || { echo 'Aufruf: scripts/schritt.sh "<Refactoring>: <was>"'; exit 2; }
[ -d src/main ] || { echo "Kein src/main. Im Projektordner aufrufen."; exit 2; }
fremd=$(git status --porcelain | grep -vE '^\?\?' | cut -c4- | awk '{print $NF}' | grep -v '^src/main/')
fremd="$fremd $(git status --porcelain | grep -E '^\?\?' | cut -c4- | grep '^src/test/')"
fremd=$(echo $fremd)
if [ -n "$fremd" ]; then
  echo "Änderungen außerhalb von src/main: $fremd"
  echo "Ein Refactoring-Schritt ändert nur Produktivcode. Tests bleiben, wie sie sind. Nichts committet."
  exit 2
fi
git add -A -- src/main
zeilen=$(git diff --cached --numstat -- src/main | awk '{a+=$1; d+=$2} END {print a+d+0}')
if [ "$zeilen" -eq 0 ]; then
  echo "Nichts geändert unter src/main. Erst das Refactoring, dann der Schritt."
  exit 2
fi
if [ "$zeilen" -gt "$max" ]; then
  git reset -q -- src/main
  echo "Zu groß für einen Schritt: $zeilen geänderte Zeilen, erlaubt sind $max."
  echo "Teile ihn: ein Refactoring je Schritt. Die Änderung bleibt im Arbeitsbaum, nichts committet."
  exit 2
fi
mkdir -p target
if mvn -q verify > target/schritt.log 2>&1; then
  git commit -q -m "$msg"
  echo "grün, committet: $(git log -1 --format='%h %s') ($zeilen Zeilen)"
  exit 0
fi
echo "rot:"
grep -E 'Tests run:.*Fail|expected|but:|Failed Approval|COMPILATION ERROR|cannot find symbol' target/schritt.log | head -6
git reset -q -- src/main
git checkout -q -- src/main
git clean -fdq -- src/main
find src/test -name '*.received.txt' -delete 2>/dev/null
echo "Produktivcode zurück auf $(git log -1 --format='%h %s'). Der Schritt hat Verhalten geändert oder kompiliert nicht: kleiner, oder anders."
exit 1
