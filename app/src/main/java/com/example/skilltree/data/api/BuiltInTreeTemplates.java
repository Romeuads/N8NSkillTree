package com.example.skilltree.data.api;

import com.example.skilltree.model.Skill;
import com.example.skilltree.model.SkillStatus;
import com.example.skilltree.model.SkillTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BuiltInTreeTemplates {

    public static SkillTree getTemplateForTheme(String rawTheme, String level, String objective, String dailyTime) {
        String themeLower = rawTheme != null ? rawTheme.trim().toLowerCase() : "";

        if (themeLower.contains("python")) {
            return createPythonTree(level, objective, dailyTime);
        } else if (themeLower.contains("android")) {
            return createAndroidTree(level, objective, dailyTime);
        } else if (themeLower.contains("banco") || themeLower.contains("sql") || themeLower.contains("database")) {
            return createDatabaseTree(level, objective, dailyTime);
        } else if (themeLower.contains("rede") || themeLower.contains("network")) {
            return createNetworksTree(level, objective, dailyTime);
        } else if (themeLower.contains("java")) {
            return createJavaTree(level, objective, dailyTime);
        } else {
            return createDynamicTree(rawTheme, level, objective, dailyTime);
        }
    }

    private static SkillTree createPythonTree(String level, String objective, String dailyTime) {
        SkillTree tree = new SkillTree();
        tree.setTitle("Python para Autodidatas");
        tree.setDescription("Trilha completa do zero à automação e análise com Python");
        tree.setTargetLevel(level != null ? level : "Iniciante");
        tree.setObjective(objective != null && !objective.isEmpty() ? objective : "Dominar lógica de programação e automação com scripts");
        tree.setDailyTime(dailyTime != null ? dailyTime : "1 hora/dia");

        List<Skill> skills = new ArrayList<>();

        skills.add(new Skill(1, "Sintaxe & Variáveis", "Fundamentos",
                "Compreender tipos primitivos (int, float, str, bool), operadores aritméticos e entrada/saída com input() e print().",
                1, 100, SkillStatus.AVAILABLE,
                Collections.emptyList(),
                Arrays.asList("Python.org Tutorial Oficial", "Curso em Vídeo - Mundo 1")));

        skills.add(new Skill(2, "Controle de Fluxo", "Fundamentos",
                "Construir tomadas de decisão utilizando if, elif, else e operadores lógicos relacionais (and, or, not).",
                2, 100, SkillStatus.LOCKED,
                Collections.singletonList(1),
                Arrays.asList("Estruturas Condicionais - W3Schools", "Exercícios práticos de lógica")));

        skills.add(new Skill(3, "Laços de Repetição", "Estruturas",
                "Automatizar repetições de blocos de instruções através de laços for (com range) e laços condicionais while.",
                2, 120, SkillStatus.LOCKED,
                Collections.singletonList(2),
                Arrays.asList("Iteradores e Loops em Python", "LeetCode: Easy Loops problems")));

        skills.add(new Skill(4, "Coleções: Listas & Tuplas", "Estruturas",
                "Armazenar sequências ordenadas de dados com listas mutáveis e tuplas imutáveis, fatiamento e métodos append/pop.",
                3, 150, SkillStatus.LOCKED,
                Collections.singletonList(3),
                Arrays.asList("Documentação Python Sequence Types", "Exercícios de Manipulação de Vetores")));

        skills.add(new Skill(5, "Dicionários & Conjuntos", "Estruturas",
                "Trabalhar com estruturas chave-valor em dicionários e conjuntos únicos (sets) para busca rápida e agregação.",
                3, 150, SkillStatus.LOCKED,
                Collections.singletonList(4),
                Arrays.asList("Python Dictionaries in Depth", "Real Python: Dictionaries & Sets")));

        skills.add(new Skill(6, "Funções & Módulos", "Modularização",
                "Definir funções com def, parâmetros posicionais, nomeados, valores padrão, escopo de variáveis e import de módulos.",
                4, 180, SkillStatus.LOCKED,
                Collections.singletonList(4),
                Arrays.asList("Python Functions Explained", "Modularização e Bibliotecas built-in (math, random, datetime)")));

        skills.add(new Skill(7, "Programação Orientada a Objetos", "Arquitetura",
                "Criar classes, instanciar objetos, utilizar atributos __init__, herança simples e encapsulamento de lógica de negócio.",
                5, 200, SkillStatus.LOCKED,
                Arrays.asList(5, 6),
                Arrays.asList("POO em Python - Guia Prático", "Projeto: Modelagem de Sistema Bancário")));

        skills.add(new Skill(8, "Projeto Prático & Automação", "Aplicações",
                "Construir um projeto completo integrando leitura de arquivos (JSON/CSV), consumo de APIs via requests e automação.",
                6, 250, SkillStatus.LOCKED,
                Collections.singletonList(7),
                Arrays.asList("Automate the Boring Stuff with Python", "GitHub: Repositório com Projeto Final")));

        tree.setSkills(skills);
        return tree;
    }

    private static SkillTree createAndroidTree(String level, String objective, String dailyTime) {
        SkillTree tree = new SkillTree();
        tree.setTitle("Desenvolvimento Android com Java");
        tree.setDescription("Arquitetura nativa Android, componentes e boas práticas acadêmicas");
        tree.setTargetLevel(level != null ? level : "Intermediário");
        tree.setObjective(objective != null && !objective.isEmpty() ? objective : "Criar aplicativos nativos Android completos em Java");
        tree.setDailyTime(dailyTime != null ? dailyTime : "1 hora/dia");

        List<Skill> skills = new ArrayList<>();

        skills.add(new Skill(1, "Ambiente & Activity", "Fundamentos",
                "Configurar o Android Studio, Gradle, compreender o AndroidManifest.xml e o ciclo de vida de uma Activity.",
                1, 100, SkillStatus.AVAILABLE,
                Collections.emptyList(),
                Arrays.asList("Android Developers: Activity Lifecycle", "Guia Android Studio")));

        skills.add(new Skill(2, "Layouts XML & Material 3", "Interface",
                "Construir interfaces responsivas com ConstraintLayout, LinearLayout, MaterialCardView e temas Material 3.",
                2, 120, SkillStatus.LOCKED,
                Collections.singletonList(1),
                Arrays.asList("Material Design 3 Guidelines", "ConstraintLayout Chains e Guidelines")));

        skills.add(new Skill(3, "Intents & Navegação", "Navegação",
                "Navegar entre telas com Intents explícitas, passar parâmetros via Bundle e tratar retornos com ActivityResultLauncher.",
                2, 120, SkillStatus.LOCKED,
                Collections.singletonList(1),
                Arrays.asList("Android Developers: Intents and Filters", "Passagem de dados com Serializable/Parcelable")));

        skills.add(new Skill(4, "RecyclerView & Adaptadores", "Listas & Dados",
                "Exibir listagens de dados dinâmicas com ViewHolder pattern, otimização de memória e eventos de clique no adapter.",
                3, 160, SkillStatus.LOCKED,
                Arrays.asList(2, 3),
                Arrays.asList("Create dynamic lists with RecyclerView", "DiffUtil e animações de lista")));

        skills.add(new Skill(5, "SQLite & Banco Local", "Persistência",
                "Implementar armazenamento local relacional com SQLiteOpenHelper, operações CRUD e consultas parametrizadas seguras.",
                4, 180, SkillStatus.LOCKED,
                Collections.singletonList(4),
                Arrays.asList("Save data using SQLite APIs", "Padrão DAO e Repository em Android")));

        skills.add(new Skill(6, "APIs REST & Conectividade", "Rede",
                "Realizar requisições assíncronas HTTP, integração com n8n/serviços externos e serialização com Gson/Retrofit.",
                5, 200, SkillStatus.LOCKED,
                Collections.singletonList(5),
                Arrays.asList("Retrofit 2 Documentation", "Consumo de Webhooks e JSON no Android")));

        skills.add(new Skill(7, "Custom Views & Canvas", "Gráficos",
                "Desenvolver componentes visuais personalizados sobrescrevendo onDraw, Paint, Path, gestos de toque e animações.",
                5, 220, SkillStatus.LOCKED,
                Collections.singletonList(5),
                Arrays.asList("Custom View Components Guide", "Canvas 2D Graphics no Android")));

        skills.add(new Skill(8, "Publicação & APK Final", "Distribuição",
                "Configurar chaves de assinatura (keystore), ProGuard/R8, geração de Release APK e boas práticas da Google Play.",
                6, 250, SkillStatus.LOCKED,
                Arrays.asList(6, 7),
                Arrays.asList("Sign your app for Google Play", "Android App Quality Guidelines")));

        tree.setSkills(skills);
        return tree;
    }

    private static SkillTree createDatabaseTree(String level, String objective, String dailyTime) {
        SkillTree tree = new SkillTree();
        tree.setTitle("Banco de Dados & SQL");
        tree.setDescription("Modelagem relacional, linguagem SQL e otimização de bancos de dados");
        tree.setTargetLevel(level != null ? level : "Iniciante");
        tree.setObjective(objective != null && !objective.isEmpty() ? objective : "Dominar modelagem DER e comandos SQL avançados");
        tree.setDailyTime(dailyTime != null ? dailyTime : "45 min/dia");

        List<Skill> skills = new ArrayList<>();

        skills.add(new Skill(1, "Modelagem Conceitual (DER)", "Modelagem",
                "Elaborar Diagramas Entidade-Relacionamento (DER), definindo entidades, atributos e cardinalidades (1:1, 1:N, N:M).",
                1, 100, SkillStatus.AVAILABLE,
                Collections.emptyList(),
                Arrays.asList("Livro Heuser: Projeto de Banco de Dados", "brModelo ou MySQL Workbench")));

        skills.add(new Skill(2, "DDL: Criação de Tabelas", "Estrutura",
                "Criar esquemas de banco com comandos CREATE TABLE, tipos de dados, chaves primárias (PRIMARY KEY) e estrangeiras (FOREIGN KEY).",
                2, 120, SkillStatus.LOCKED,
                Collections.singletonList(1),
                Arrays.asList("W3Schools SQL CREATE TABLE", "Restrições de Integridade Referencial")));

        skills.add(new Skill(3, "DML: CRUD & Comandos Básicos", "Manipulação",
                "Inserir, atualizar, consultar e deletar dados em tabelas utilizando INSERT, UPDATE, DELETE e SELECT com cláusula WHERE.",
                2, 130, SkillStatus.LOCKED,
                Collections.singletonList(2),
                Arrays.asList("PostgreSQL Tutorial: Basic CRUD", "SQL Bolt Interactive Exercises")));

        skills.add(new Skill(4, "Relacionamentos & JOINs", "Consultas",
                "Cruzar dados entre múltiplas tabelas normalizadas através de INNER JOIN, LEFT JOIN, RIGHT JOIN e FULL OUTER JOIN.",
                3, 160, SkillStatus.LOCKED,
                Collections.singletonList(3),
                Arrays.asList("Visual Explanation of SQL Joins", "Exercícios práticos de junção múltipla")));

        skills.add(new Skill(5, "Agregações & Subconsultas", "Análise",
                "Calcular estatísticas e sumários com GROUP BY, HAVING, funções agregadas (COUNT, SUM, AVG) e subqueries correlacionadas.",
                4, 180, SkillStatus.LOCKED,
                Collections.singletonList(4),
                Arrays.asList("SQL Aggregate Functions", "HackerRank: SQL Intermediate Challenges")));

        skills.add(new Skill(6, "Transações & Índices", "Performance",
                "Garantir propriedades ACID com transações (BEGIN, COMMIT, ROLLBACK) e otimizar velocidade de busca com índices B-Tree.",
                5, 220, SkillStatus.LOCKED,
                Collections.singletonList(5),
                Arrays.asList("PostgreSQL Indexes Explained", "Use the Index, Luke!")));

        tree.setSkills(skills);
        return tree;
    }

    private static SkillTree createNetworksTree(String level, String objective, String dailyTime) {
        SkillTree tree = new SkillTree();
        tree.setTitle("Redes de Computadores");
        tree.setDescription("Arquitetura de redes, protocolos da Internet e infraestrutura");
        tree.setTargetLevel(level != null ? level : "Iniciante");
        tree.setObjective(objective != null && !objective.isEmpty() ? objective : "Compreender o funcionamento da pilha TCP/IP e infraestrutura de rede");
        tree.setDailyTime(dailyTime != null ? dailyTime : "1 hora/dia");

        List<Skill> skills = new ArrayList<>();

        skills.add(new Skill(1, "Modelos OSI & TCP/IP", "Fundamentos",
                "Compreender as camadas de comunicação, encapsulamento de pacotes e a diferença entre os modelos conceituais.",
                1, 100, SkillStatus.AVAILABLE,
                Collections.emptyList(),
                Arrays.asList("Kurose: Redes de Computadores", "Cisco Networking Basics")));

        skills.add(new Skill(2, "Endereçamento IPv4 & Máscaras", "Camada de Rede",
                "Calcular endereços de rede, classes IP, máscaras CIDR e divisão de redes em sub-redes para otimização de tráfego.",
                2, 130, SkillStatus.LOCKED,
                Collections.singletonList(1),
                Arrays.asList("IPv4 Subnetting Guide", "Subnetting Practice Drills")));

        skills.add(new Skill(3, "Protocolos de Transporte: TCP vs UDP", "Camada de Transporte",
                "Analisar o handshake triplo do TCP, controle de fluxo, retransmissão confiável e casos de uso de baixa latência com UDP.",
                3, 150, SkillStatus.LOCKED,
                Collections.singletonList(2),
                Arrays.asList("Wireshark: Análise de Pacotes TCP/UDP", "RFC 793 - TCP Specification")));

        skills.add(new Skill(4, "Serviços Essenciais: DNS & DHCP", "Camada de Aplicação",
                "Configurar e compreender a resolução de nomes de domínio (DNS) e alocação dinâmica de IPs por servidores DHCP.",
                4, 160, SkillStatus.LOCKED,
                Collections.singletonList(3),
                Arrays.asList("How DNS Works Comic", "Configuração de Servidor DHCP")));

        skills.add(new Skill(5, "Roteamento & Switches", "Infraestrutura",
                "Diferenciar domínios de colisão e broadcast em Switches L2 e compreender protocolos de roteamento dinâmico (OSPF, BGP).",
                5, 190, SkillStatus.LOCKED,
                Collections.singletonList(4),
                Arrays.asList("Cisco Packet Tracer Labs", "Routing Protocols Overview")));

        skills.add(new Skill(6, "Segurança & Firewalls", "Segurança",
                "Implementar regras de filtragem de pacotes, NAT (Network Address Translation), VPNs e criptografia com TLS/HTTPS.",
                6, 220, SkillStatus.LOCKED,
                Collections.singletonList(5),
                Arrays.asList("OWASP Network Security", "Configuração de Regras no iptables/pfSense")));

        tree.setSkills(skills);
        return tree;
    }

    private static SkillTree createJavaTree(String level, String objective, String dailyTime) {
        SkillTree tree = new SkillTree();
        tree.setTitle("Java Orientado a Objetos");
        tree.setDescription("Sintaxe, padrões de projeto e desenvolvimento corporativo em Java");
        tree.setTargetLevel(level != null ? level : "Iniciante");
        tree.setObjective(objective != null && !objective.isEmpty() ? objective : "Dominar programação orientada a objetos profissional com Java");
        tree.setDailyTime(dailyTime != null ? dailyTime : "1 hora/dia");

        List<Skill> skills = new ArrayList<>();

        skills.add(new Skill(1, "Fundamentos da Linguagem", "Sintaxe",
                "Tipos primitivos, variáveis, controle de fluxo (if, switch-case), laços for/while e método main.",
                1, 100, SkillStatus.AVAILABLE,
                Collections.emptyList(),
                Arrays.asList("Oracle Java Tutorials", "Java Como Programar - Deitel")));

        skills.add(new Skill(2, "Classes & Objetos", "POO Básica",
                "Definição de atributos e métodos, instanciação com new, construtores e palavra-chave this.",
                2, 120, SkillStatus.LOCKED,
                Collections.singletonList(1),
                Arrays.asList("POO em Java - Apostila Caelum", "Modelagem de classes simples")));

        skills.add(new Skill(3, "Encapsulamento & Modificadores", "POO Intermediária",
                "Modificadores de acesso (public, private, protected), métodos getters/setters e integridade do estado do objeto.",
                3, 140, SkillStatus.LOCKED,
                Collections.singletonList(2),
                Arrays.asList("Java Encapsulation Best Practices", "Imutabilidade em Java")));

        skills.add(new Skill(4, "Herança & Polimorfismo", "POO Avançada",
                "Reutilização com extends, sobrescrita com @Override, classes abstratas e despacho dinâmico de métodos.",
                4, 180, SkillStatus.LOCKED,
                Collections.singletonList(3),
                Arrays.asList("Polymorphism in Java Guide", "Exercícios de hierarquia de classes")));

        skills.add(new Skill(5, "Interfaces & Coleções", "Estruturas",
                "Contratos com interfaces, List, ArrayList, Set, Map, Generics e iteração com Enhanced For e Streams.",
                5, 200, SkillStatus.LOCKED,
                Collections.singletonList(4),
                Arrays.asList("Java Collections Framework Overview", "Baeldung: Guide to Java List & Map")));

        skills.add(new Skill(6, "Tratamento de Exceções & I/O", "Robustez",
                "Tratamento de erros com try-catch-finally, checked vs unchecked exceptions, criação de exceções personalizadas.",
                6, 220, SkillStatus.LOCKED,
                Collections.singletonList(5),
                Arrays.asList("Effective Java - Item sobre Exceções", "Try-with-resources statement")));

        tree.setSkills(skills);
        return tree;
    }

    private static SkillTree createDynamicTree(String theme, String level, String objective, String dailyTime) {
        String cleanTheme = (theme != null && !theme.trim().isEmpty()) ? theme.trim() : "Conhecimento Geral";
        SkillTree tree = new SkillTree();
        tree.setTitle(cleanTheme);
        tree.setDescription("Trilha de aprendizagem progressiva estruturada para " + cleanTheme);
        tree.setTargetLevel(level != null ? level : "Iniciante");
        tree.setObjective(objective != null && !objective.isEmpty() ? objective : "Dominar as principais competências de " + cleanTheme);
        tree.setDailyTime(dailyTime != null ? dailyTime : "1 hora/dia");

        List<Skill> skills = new ArrayList<>();

        // Stage 1
        skills.add(new Skill(1, "Conceitos Básicos & Vocabulário", "Fundamentos",
                "Compreender a terminologia essencial, princípios teóricos e o ambiente de prática inicial em " + cleanTheme + ".",
                1, 100, SkillStatus.AVAILABLE,
                Collections.emptyList(),
                Arrays.asList("Documentação introdutória e guias básicos", "Vídeos e tutoriais para iniciantes")));

        // Stage 2
        skills.add(new Skill(2, "Ferramental & Prática Inicial", "Prática",
                "Configurar ferramentas de trabalho recomendadas e executar os primeiros exercícios guiados de " + cleanTheme + ".",
                2, 120, SkillStatus.LOCKED,
                Collections.singletonList(1),
                Arrays.asList("Configuração do ambiente de estudo", "Lista de exercícios práticos introdutórios")));

        // Stage 3
        skills.add(new Skill(3, "Técnicas Intermediárias", "Desenvolvimento",
                "Aplicar métodos estruturados, resolver problemas comuns e aprofundar a compreensão das nuances de " + cleanTheme + ".",
                3, 150, SkillStatus.LOCKED,
                Collections.singletonList(2),
                Arrays.asList("Artigos técnicos e estudos de caso", "Workshops e cursos intermediários")));

        // Stage 4
        skills.add(new Skill(4, "Padrões & Boas Práticas", "Qualidade",
                "Adotar convenções reconhecidas pela indústria, otimização de processos e evitar erros frequentes em " + cleanTheme + ".",
                4, 180, SkillStatus.LOCKED,
                Collections.singletonList(3),
                Arrays.asList("Manuais de boas práticas da comunidade", "Exemplos de código e projetos de referência")));

        // Stage 5
        skills.add(new Skill(5, "Projeto Integrador", "Aplicação Real",
                "Construir um projeto prático completo do início ao fim aplicando as competências adquiridas em " + cleanTheme + ".",
                5, 220, SkillStatus.LOCKED,
                Collections.singletonList(4),
                Arrays.asList("Desafio prático guiado", "Portfólio de projetos aplicados")));

        // Stage 6
        skills.add(new Skill(6, "Domínio & Especialização", "Maestria",
                "Explorar tópicos avançados, integração com outras áreas do conhecimento e consolidação profissional em " + cleanTheme + ".",
                6, 250, SkillStatus.LOCKED,
                Collections.singletonList(5),
                Arrays.asList("Publicações científicas e novidades da área", "Comunidades avançadas e fóruns de discussão")));

        tree.setSkills(skills);
        return tree;
    }
}
