# Konaet Cover para Android

Aplicativo Android do Konaet Cover, escrito em Kotlin e Jetpack Compose. A versão `0.1.0-alpha.3` é uma demonstração navegável: não movimenta dinheiro, não oferece seguro e funciona sem servidor pelo botão **Entrar no modo demonstração**.

## Requisitos

- JDK 17;
- Android SDK 36 e Build Tools 36.0.0;
- o Gradle Wrapper versionado neste diretório.

## Compilar e verificar

Linux/macOS:

```bash
./gradlew test lintDevDebug assembleDevDebug bundleProdRelease
```

PowerShell:

```powershell
.\gradlew.bat test lintDevDebug assembleDevDebug bundleProdRelease
```

Saídas principais:

- APK instalável de demonstração: `app/build/outputs/apk/dev/debug/app-dev-debug.apk`;
- Android App Bundle de validação: `app/build/outputs/bundle/prodRelease/app-prod-release.aab`.

O APK de demonstração usa a chave de debug do Android. Ele serve para instalação e testes, mas não pode ser usado como artefato final da Play Store.

## Assinar o bundle para a Play Store

Nunca adicione o keystore, aliases ou senhas ao Git. Configure as variáveis abaixo somente no ambiente seguro de release:

```text
KONAET_UPLOAD_STORE_FILE
KONAET_UPLOAD_STORE_PASSWORD
KONAET_UPLOAD_KEY_ALIAS
KONAET_UPLOAD_KEY_PASSWORD
```

Depois execute:

```bash
./gradlew clean bundleProdRelease
```

Quando as quatro variáveis estão presentes, o Gradle assina o bundle com a chave de upload. Para uma aplicação nova, habilite o Play App Signing e mantenha uma cópia segura e recuperável da chave de upload.

## Variantes

| Variante | Identificador | Uso |
|---|---|---|
| `devDebug` | `com.konaet.cover.dev` | demonstração e testes locais |
| `stagingDebug` | `com.konaet.cover.staging` | homologação futura |
| `prodRelease` | `com.konaet.cover` | bundle de produção |

## Segurança e privacidade

O manifesto de produção não solicita permissões. A permissão de internet existe somente na variante de desenvolvimento, fora do AAB enviado à Google Play. Consulte a [política de privacidade](../../docs/PRIVACY.md) e o [checklist da Play Store](../../docs/PLAY_STORE_CHECKLIST.md).
