#!/bin/bash
# Genehmigt alle *.received.txt unter src/test: sie werden zu *.approved.txt. Vorher lesen, was drinsteht.
set -u
n=0
while IFS= read -r -d '' f; do
  mv "$f" "${f%.received.txt}.approved.txt"
  echo "genehmigt: ${f%.received.txt}.approved.txt"
  n=$((n+1))
done < <(find src/test -name '*.received.txt' -print0)
[ "$n" -eq 0 ] && echo "Nichts zu genehmigen: keine *.received.txt unter src/test."
exit 0
