#!/bin/bash
# Nummeriert die Regeln des Anforderungsdokuments (jede Zeile, die mit "* " beginnt, bis zur Überschrift "Credits")
# und zählt je Regel, wie oft Tests unter src/test sie nennen: Kommentarzeile, die mit R<n> beginnt, etwa `// R4:` oder `// R4, R5:`. Je Satz die Zahlen
# (Kandidaten für Schwellen) und Signalwörter (never, every, twice: Kandidaten für Ränder). Je Zahl, die ein Test
# als Eingabe benutzt, ob auch ein Nachbar (Wert davor oder danach) als Eingabe vorkommt.
# Regeln ohne Test und Schwellen ohne Nachbarn stehen in der Zusammenfassung.
# Aufruf: scripts/regeln.sh [Dokument]   (Standard: GildedRoseKata.md)
set -u
doc=${1:-GildedRoseKata.md}
[ -f "$doc" ] || { echo "Kein Dokument $doc."; exit 1; }
tests=src/test/java
tag() { printf '^[[:space:]]*//[[:space:]]*(R[0-9]+[[:space:]]*,[[:space:]]*)*R%s([^0-9]|$)' "$1"; }
nennungen() { grep -rhE "$(tag "$1")" "$tests" 2>/dev/null | wc -l | tr -d ' '; }
# Eingaben: Zahlen in Konstruktoraufrufen (new X(...)) der Tests, deren Kommentarblock R<n> nennt; Texte in Anführungszeichen zählen nicht
eingaben() {
  find "$tests" -name '*.java' -print0 2>/dev/null | xargs -0 awk -v t="$(tag "$1")" '
    FNR == 1 { block = 0; cur = 0 }
    /^[[:space:]]*\/\// { if (!block) { block = 1; cur = 0 }; if ($0 ~ t) cur = 1; next }
    { block = 0 }
    cur && /new [A-Z][A-Za-z0-9_]*\(/ { l = $0; gsub(/"[^"]*"/, "", l); while (match(l, /-?[0-9]+/)) { print substr(l, RSTART, RLENGTH); l = substr(l, RSTART + RLENGTH) } }
  ' | sort -un | tr '\n' ' '
}
n=0; ohne=""; nachbarn=""
while IFS= read -r zeile; do
  n=$((n+1))
  anzahl=$(nennungen "$n")
  printf 'R%-2d %2s Tests  %s\n' "$n" "$anzahl" "${zeile#\* }"
  zahlen=$(printf '%s' "$zeile" | grep -oE '[0-9]+' | awk '!s[$0]++' | tr '\n' ' ')
  woerter=$(printf '%s' "$zeile" | grep -oiwE 'never|always|every|each|all|once|twice|less|more|after|before|drops' | tr 'A-Z' 'a-z' | awk '!s[$0]++' | paste -sd ',' - | sed 's/,/, /g')
  [ -n "$zahlen$woerter" ] && printf '              Zahlen: %s   Wörter: %s\n' "$(echo ${zahlen:--} | sed 's/ /, /g')" "${woerter:--}"
  if [ "$anzahl" -eq 0 ]; then
    ohne="$ohne R$n"
  elif [ -n "$zahlen" ]; then
    e=" $(eingaben "$n")"
    status=""
    for z in $zahlen; do
      case "$e" in *" $z "*) ;; *) continue ;; esac
      v=$((z-1)); h=$((z+1)); jv=nein; jh=nein
      case "$e" in *" $v "*) jv=ja ;; esac
      case "$e" in *" $h "*) jh=ja ;; esac
      status="$status  $z: davor $v $jv, danach $h $jh."
      [ "$jv" = nein ] && [ "$jh" = nein ] && nachbarn="$nachbarn R$n($z)"
    done
    [ -n "$status" ] && printf '              Schwellen in den Tests:%s\n' "$status"
  fi
done < <(sed '/^Credits$/,$d' "$doc" | grep -E '^\* ')
for k in $(grep -rhE '^[[:space:]]*//[[:space:]]*R[0-9]' "$tests" 2>/dev/null | sed -E 's|^[[:space:]]*//[[:space:]]*||; s/:.*//' | grep -oE 'R[0-9]+' | grep -oE '[0-9]+' | sort -un); do
  [ "$k" -gt "$n" ] && printf 'R%-2d %2s Tests  (Regel aus dem Fließtext, nicht in der Liste)\n' "$k" "$(nennungen "$k")"
done
echo "Zusammenfassung: $n Regeln in der Liste, ohne Test:${ohne:- keine}; Schwellen ohne Nachbarn:${nachbarn:- keine}"
