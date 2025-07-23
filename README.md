# Tasks

## Descrição

Este é um aplicativo Android desenvolvido em Kotlin e Java, utilizando Gradle como sistema de build. O projeto tem como objetivo gerenciar tarefas, permitindo aos usuários criar, editar e visualizar suas tarefas de forma organizada.

## Tecnologias Utilizadas

- Kotlin
- Java
- Gradle
- Android Architecture Components (ViewModel, LiveData, Room)
- Retrofit
- Fingerprint API

## Estrutura do Projeto

O projeto está organizado da seguinte forma:

-   `app/src/main/java/com/example/tasks/`: Contém o código fonte principal do aplicativo.
    -   `service/`: Camada de serviços.
        -   `constants/`: Constantes utilizadas no projeto.
            -   `TaskConstants.kt`: Constantes relacionadas às tarefas.
        -   `helper/`: Classes auxiliares.
            -   `FingerprintHelper.kt`: Auxiliar para autenticação por impressão digital.
        -   `listener/`: Interfaces de listener.
            -   `APIListener.kt`: Listener para respostas da API.
            -   `TaskListener.kt`: Listener para eventos de tarefas.
        -   `model/`: Classes de modelo de dados.
            -   `PersonModel.kt`: Modelo para dados de pessoa.
            -   `PriorityModel.kt`: Modelo para prioridades de tarefas.
            -   `TaskModel.kt`: Modelo para dados de tarefas.
            -   `ValidationModel.kt`: Modelo para validação de dados.
        -   `repository/`: Camada de repositório.
            -   `BaseRepository.kt`: Classe base para repositórios.
            -   `PersonRepository.kt`: Repositório para dados de pessoa.
            -   `PriorityRepository.kt`: Repositório para prioridades de tarefas.
            -   `SecurityPreferences.kt`: Classe para gerenciar preferências de segurança.
            -   `TaskRepository.kt`: Repositório para dados de tarefas.
            -   `local/`: Classes para acesso ao banco de dados local.
                -   `PriorityDAO.kt`: Data Access Object para prioridades.
                -   `TaskDatabase.kt`: Classe para o banco de dados Room.
            -   `remote/`: Classes para acesso à API remota.
                -   `PersonService.kt`: Serviço para dados de pessoa.
                -   `PriorityService.kt`: Serviço para prioridades de tarefas.
                -   `RetrofitClient.kt`: Cliente Retrofit para chamadas à API.
                -   `TaskService.kt`: Serviço para dados de tarefas.
        -   `view/`: Camada de visualização (Activities e Fragments).
            -   `AllTasksFragment.kt`: Fragment para listar todas as tarefas.
            -   `LoginActivity.kt`: Activity para tela de login.
            -   `MainActivity.kt`: Activity principal do aplicativo.
            -   `RegisterActivity.kt`: Activity para tela de registro.
            -   `TaskFormActivity.kt`: Activity para formulário de tarefas.
            -   `adapter/`: Adaptadores para RecyclerView.
                -   `TaskAdapter.kt`: Adaptador para a lista de tarefas.
            -   `viewholder/`: ViewHolders para RecyclerView.
                -   `TaskViewHolder.kt`: ViewHolder para itens de tarefa.
        -   `viewmodel/`: Camada de ViewModel.
            -   `AllTasksViewModel.kt`: ViewModel para o fragmento de todas as tarefas.
            -   `LoginViewModel.kt`: ViewModel para a tela de login.
            -   `MainViewModel.kt`: ViewModel para a activity principal.
            -   `RegisterViewModel.kt`: ViewModel para a tela de registro.
            -   `TaskFormViewModel.kt`: ViewModel para o formulário de tarefas.
    -   `app/res/`: Recursos do aplicativo (layouts, drawables, etc.).
    -   `app/proguard-rules.pro`: Regras do ProGuard para otimização e ofuscação do código.
    -   `build.gradle`: Arquivo de configuração do Gradle para o módulo do aplicativo.
    -   `settings.gradle`: Arquivo de configuração do Gradle para o projeto.

## Funcionalidades

-   [x] Criação de tarefas com título, descrição e prioridade.
-   [x] Listagem de tarefas com opção de filtro por prioridade.
-   [x] Edição e exclusão de tarefas.
-   [x] Autenticação de usuários (login e registro).
-   [x] Autenticação por impressão digital.
-   [ ] Sincronização de tarefas com um servidor remoto.

## Como Executar

1.  Clone o repositório:

    ```bash
    git clone https://github.com/seu-usuario/seu-repositorio.git
    ```
2.  Abra o projeto no Android Studio.
3.  Aguarde o Gradle sincronizar e baixar as dependências.
4.  Execute o aplicativo em um emulador ou dispositivo físico.

## Configuração do ProGuard

O arquivo `proguard-rules.pro` contém as regras para o ProGuard, que é usado para otimizar e ofuscar o código. Atualmente, ele inclui:

-   Comentários padrão para configuração.
-   Instruções para habilitar a preservação de informações de número de linha (comentado por padrão).
-   Instruções para renomear o arquivo fonte (comentado por padrão).

Para mais detalhes sobre o ProGuard, consulte a [documentação oficial](http://developer.android.com/guide/developing/tools/proguard.html).


## Badges

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://exemplo.com/build)
[![License](https://img.shields.io/badge/license-MIT-blue)](LICENSE)
[![Kotlin](https://img.shields.io/badge/language-Kotlin-orange)](https://kotlinlang.org)

## Contato

[Seu Nome] - [seu-email@exemplo.com]

## Agradecimentos

Agradecimentos a [pessoas ou organizações que ajudaram no projeto].
