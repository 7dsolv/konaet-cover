# Checklist para publicação na Google Play

## Pronto no repositório

- [x] identificador de produção `com.konaet.cover`;
- [x] `compileSdk` e `targetSdk` 36;
- [x] `versionCode` 2 e `versionName` `0.1.0-alpha.2`;
- [x] ícone adaptativo e nome do aplicativo;
- [x] apenas permissão de internet no manifesto de produção;
- [x] minificação e redução de recursos na variante `prodRelease`;
- [x] Gradle Wrapper 8.13 com checksum da distribuição;
- [x] build, testes, lint, APK e AAB executados pelo GitHub Actions;
- [x] fluxo manual que instala e abre o APK em emulador Android 15;
- [x] configuração de assinatura por variáveis de ambiente, sem segredos no Git;
- [x] política de privacidade pública;
- [x] rascunho de descrição e notas de versão em [`store-listing/pt-BR.md`](store-listing/pt-BR.md).
- [x] ícone de loja 512 × 512, feature graphic 1024 × 500 e sete screenshots reais 1080 × 1920 em [`store-listing/graphics`](store-listing/graphics).

## Ações manuais antes do envio

- [ ] concluir a verificação da conta de desenvolvedor no Play Console;
- [ ] criar o aplicativo e confirmar a disponibilidade de `com.konaet.cover`;
- [ ] gerar uma chave de upload, armazená-la em cofre seguro e habilitar Play App Signing;
- [ ] assinar o AAB de produção e testar a assinatura;
- [ ] definir uma API HTTPS de produção e revisar autenticação, exclusão e retenção de dados;
- [ ] preencher Data Safety, classificação de conteúdo, público-alvo e acesso ao app;
- [ ] executar teste interno fechado em dispositivos físicos;
- [ ] revisar acessibilidade, comportamento offline, ANRs e relatório de pré-lançamento;
- [ ] enviar para revisão somente depois de validar aspectos jurídicos e regulatórios.

## Regra de assinatura

O APK público de demonstração é assinado com a chave de debug e não deve ser enviado à Play Store. O artefato correto para a loja é o `prodRelease.aab` assinado com a chave de upload. A chave e suas senhas nunca devem ser publicadas em releases, artifacts, logs ou commits.
