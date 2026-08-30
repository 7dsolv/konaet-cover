# Como contribuir

Obrigado por ajudar o Konaet Cover. O projeto aceita forks, issues e pull requests de todos os níveis de experiência.

## Fluxo recomendado

1. Faça um fork do repositório.
2. Crie uma branch curta: `feat/nome`, `fix/nome` ou `docs/nome`.
3. Faça uma mudança focada e inclua testes.
4. Execute as verificações relevantes.
5. Abra um pull request explicando problema, solução, evidências e riscos.

```bash
corepack pnpm install
corepack pnpm run build
corepack pnpm run lint
corepack pnpm run format:check
corepack pnpm run test:api
corepack pnpm run test:risk
```

Contratos exigem Foundry:

```bash
corepack pnpm run test:contracts
```

## Regras essenciais

- Preserve o modo `DEMO/SIMULATION`.
- Nunca envie tokens, chaves, senhas, dados pessoais ou identificadores reais de dispositivos.
- Não apresente o modelo de risco como cálculo atuarial validado.
- Use matemática compatível com GitHub Markdown: `$...$` para expressão inline e blocos `math` para fórmulas destacadas.
- Atualize documentação e testes quando o comportamento mudar.
- Trate participantes com respeito; consulte o [`CODE_OF_CONDUCT.md`](CODE_OF_CONDUCT.md).

## Boas primeiras contribuições

Procure issues com `good first issue`, `documentation` ou `help wanted`. Também são úteis:

- testes de unidade e integração;
- acessibilidade no Android;
- validação de entrada da API;
- documentação em português e inglês;
- reprodutibilidade e explicação do modelo matemático;
- modelagem de ameaças e privacidade.
