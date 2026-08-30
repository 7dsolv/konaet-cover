# Política de segurança

## Versões suportadas

O Konaet Cover está em estágio experimental. Somente a branch `main` recebe correções de segurança.

## Relatar uma vulnerabilidade

Não publique detalhes sensíveis em issues. Abra um [aviso privado de segurança](https://github.com/7dsolv/konaet-cover/security/advisories/new) com:

- componente e versão afetados;
- impacto provável;
- passos mínimos para reprodução;
- sugestão de correção, quando disponível.

O recebimento será confirmado assim que possível. A correção e a divulgação serão coordenadas no próprio advisory.

## Limites atuais

- O sistema opera somente em `DEMO/SIMULATION`.
- Não existe auditoria externa do contrato Solidity.
- O modelo de risco usa coeficientes sintéticos e não é atuarial.
- Autenticação, armazenamento de evidências e integrações regulatórias ainda estão em evolução.
- Segredos devem existir apenas em `.env` local ou no cofre de secrets do GitHub.

Consulte [`docs/ROADMAP.md`](docs/ROADMAP.md) antes de avaliar prontidão para produção.
