# PlayLog 🎬

PlayLog é um aplicativo Android nativo desenvolvido em Kotlin que permite aos usuários rastrear as séries de TV que estão assistindo. Com ele, você pode buscar por séries, ver detalhes, acompanhar seu progresso e muito mais.

## 🚀 Funcionalidades Principais

Com base nas telas e componentes do projeto, as principais funcionalidades são:

  * **📺 Tela Principal (Home):**

      * Exibe a última série que você assistiu.
      * Mostra um carrossel com outras séries que você já acompanhou.
      * Navegação para ver todas as séries já assistidas.

  * **🔍 Busca de Séries:**

      * Uma tela dedicada para pesquisar novas séries para adicionar à sua lista.
      * Utiliza a API do The Movie Database (TMDB) para obter informações das séries.

  * **📄 Detalhes da Série:**

      * Mostra a imagem de capa, título e descrição da série.
      * Exibe o último episódio assistido.
      * Permite atualizar o progresso do episódio atual.
      * Opção para descartar o progresso de uma série.

  * **⚙️ Configurações:**

      * Opção para deletar todos os dados do aplicativo.
      * Funcionalidades de backup e restauração dos seus dados.

## 🛠️ Dependências e Tecnologias

O projeto foi construído utilizando as seguintes tecnologias e bibliotecas:

  * **Linguagem:** [Kotlin](https://kotlinlang.org/)
  * **Arquitetura:** Baseada em Activities e Views com XML.
  * **Interface Gráfica:**
      * **Material Components:** Para os componentes de UI, como botões e cards.
      * **RecyclerView:** Para a exibição de listas e carrosséis.
  * **Rede:**
      * **Retrofit:** Para fazer as chamadas à API do TMDB.
      * **Moshi:** Para fazer o parse do JSON da API para objetos Kotlin.
      * **OkHttp Logging Interceptor:** Para depurar as chamadas de rede.
  * **Banco de Dados Local:**
      * **Room:** Para persistir os dados das séries localmente no dispositivo.
  * **Carregamento de Imagens:**
      * **Coil e Glide:** Para carregar e exibir imagens da internet de forma eficiente.
  * **Gerenciamento de Dependências:**
      * **Gradle** com **Version Catalog** (`libs.versions.toml`).

## 👨‍💻 Como Rodar o Projeto

Para conseguir rodar este projeto no seu computador, siga os passos abaixo.

### Pré-requisitos

  * [Android Studio](https://developer.android.com/studio) instalado.
  * Um emulador Android ou um dispositivo físico.

### Configuração

1.  **Clone o Repositório:**

    ```bash
    git clone https://github.com/04sPaula/PlayLog.git
    ```

2.  **Obtenha uma Chave da API do TMDB:**

      * Crie uma conta no site do [The Movie Database (TMDB)](https://www.themoviedb.org/).
      * Vá para as **Configurações** da sua conta e clique na aba **API**.
      * Gere uma nova chave de API (v3 auth).

3.  **Adicione a Chave da API ao Projeto:**

      * Na raiz do projeto, encontre ou crie um arquivo chamado `local.properties`.
      * Dentro deste arquivo, adicione a seguinte linha, substituindo `SUA_CHAVE_API_AQUI` pela chave que você gerou no passo anterior:
        ```properties
        tmdb_api_key=SUA_CHAVE_API_AQUI
        ```
      * **Importante:** O arquivo `local.properties` não deve ser enviado para o seu repositório do Git, pois ele contém informações sensíveis.

4.  **Abra e Execute o Projeto:**

      * Abra o projeto no Android Studio.
      * O Gradle irá sincronizar e baixar todas as dependências automaticamente.
      * Clique no botão "Run" (▶️) para instalar e executar o aplicativo no seu emulador ou dispositivo.
