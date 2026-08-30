#!/bin/sh
set -eu

mkdir -p store-screenshots

dump_ui() {
  adb shell uiautomator dump /sdcard/window.xml >/dev/null
  adb exec-out cat /sdcard/window.xml > window.xml
}

wait_for_text() {
  label="$1"
  attempt=1
  while [ "$attempt" -le 20 ]; do
    dump_ui
    if grep -Fq "text=\"${label}\"" window.xml; then
      return 0
    fi
    attempt=$((attempt + 1))
    sleep 1
  done

  echo "Texto não encontrado na interface: ${label}" >&2
  cat window.xml >&2
  return 1
}

tap_text() {
  label="$1"
  wait_for_text "$label"

  bounds="$(
    sed 's/></>\n</g' window.xml |
      grep -F "text=\"${label}\"" |
      head -n 1 |
      sed -n 's/.*bounds="\[\([0-9]*\),\([0-9]*\)\]\[\([0-9]*\),\([0-9]*\)\]".*/\1 \2 \3 \4/p'
  )"

  if [ -z "$bounds" ]; then
    echo "Limites não encontrados para: ${label}" >&2
    return 1
  fi

  set -- $bounds
  x1="$1"
  y1="$2"
  x2="$3"
  y2="$4"
  adb shell input tap "$(((x1 + x2) / 2))" "$(((y1 + y2) / 2))"
}

capture() {
  filename="$1"
  sleep 1
  adb exec-out screencap -p > "store-screenshots/${filename}"
}

adb install -r apps/android/app/build/outputs/apk/dev/debug/app-dev-debug.apk
adb shell wm size 1080x1920
adb shell wm density 420
adb shell am start -W -n com.konaet.cover.dev/com.konaet.cover.MainActivity
adb shell dumpsys activity activities | grep -q "com.konaet.cover.dev"

wait_for_text "EXPLORAR O KONAET"
capture "01-onboarding.png"

tap_text "EXPLORAR O KONAET"
wait_for_text "ENTRAR NO MODO DEMONSTRAÇÃO"
capture "02-demo-access.png"

tap_text "ENTRAR NO MODO DEMONSTRAÇÃO"
wait_for_text "PROTEÇÃO VERIFICÁVEL"
capture "03-protection-status.png"

tap_text "Pools"
wait_for_text "POOLS DE PROTEÇÃO"
capture "04-protection-pools.png"

tap_text "Eventos"
wait_for_text "EVENTOS E EVIDÊNCIAS"
capture "05-events.png"

tap_text "Risco"
wait_for_text "LABORATÓRIO DE RISCO"
capture "06-risk-lab.png"

tap_text "Perfil"
wait_for_text "PERFIL DE DEMONSTRAÇÃO"
capture "07-profile.png"
