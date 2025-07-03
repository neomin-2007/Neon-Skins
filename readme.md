# 🎀 NEON SKINS - Seu plugin de skins avançado para Minecraft 1.8.9

![GitHub](https://img.shields.io/github/license/seu-usuario/neon-skins?color=ff69b4)
![GitHub release](https://img.shields.io/github/v/release/seu-usuario/neon-skins?color=ff69b4)
![GitHub issues](https://img.shields.io/github/issues/seu-usuario/neon-skins?color=ff69b4)

✨ Um plugin completo e otimizado para gerenciamento de skins em servidores Minecraft 1.8.9, com suporte a múltiplas fontes e armazenamento eficiente.

## 🧪 Compatibilidade

| Versão | Status       |
|--------|--------------|
| 1.8.9  | ✅ Suportado |
| Outras | 🚧 Planejado |

> 📌 Suporte para versões mais recentes está em desenvolvimento ativo!

## 🛠 Recursos Principais

### 🔧 Funcionalidades
- ✅ Configuração personalizável via arquivo YAML
- 🌐 Conexão direta com a API oficial da Mojang
- 🖼️ Integração com a API do [MineSkin](https://mineskin.org)
- 💾 Suporte a múltiplos bancos de dados:
  - SQLite (padrão)
  - MySQL/MariaDB
- 📦 Sistemas de cache inteligente:
  - Cache de usuário (dados temporários)
  - Cache de skins recentes
- 🎭 Suporte a skins customizadas por:
  - URL de imagens
  - Arquivos locais
  - Nomes de jogadores

## ⚡ Performance

O plugin foi desenvolvido com foco em eficiência:
- 🚀 Baixo consumo de recursos
- ⏳ Tempos de resposta rápidos
- 📊 Sistema de cache configurável
- 🔄 Atualizações assíncronas para não travar o servidor

## 🖼️ Mostruário

![Exemplo de Skin](https://exemplo.com/skin-example.png) *(Imagem ilustrativa)*

## 📥 Instalação

1. Baixe o arquivo .jar mais recente na seção [Releases](https://github.com/seu-usuario/neon-skins/releases)
2. Coloque na pasta `plugins` do seu servidor
3. Reinicie o servidor
4. Configure conforme necessário no arquivo `plugins/NeonSkins/config.yml`

## ⚙️ Configuração

Exemplo básico de configuração:
```yaml
database:
  type: "sqlite" # ou "mysql"
  mysql:
    host: "localhost"
    port: 3306
    database: "neonskins"
    username: "usuario"
    password: "senha"

cache:
  player_cache_time: 3600 # 1 hora em segundos
  skin_cache_time: 86400 # 24 horas em segundos
