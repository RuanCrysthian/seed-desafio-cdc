-- Seed data for testing

-- Inserindo países
INSERT INTO paises (pais_id, nome) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Brasil'),
('550e8400-e29b-41d4-a716-446655440002', 'Estados Unidos'),
('550e8400-e29b-41d4-a716-446655440003', 'Argentina'),
('550e8400-e29b-41d4-a716-446655440004', 'Chile'),
('550e8400-e29b-41d4-a716-446655440005', 'México');

-- Inserindo estados
INSERT INTO estados (estado_id, nome, pais_id) VALUES
('550e8400-e29b-41d4-a716-446655441001', 'São Paulo', '550e8400-e29b-41d4-a716-446655440001'),
('550e8400-e29b-41d4-a716-446655441002', 'Rio de Janeiro', '550e8400-e29b-41d4-a716-446655440001'),
('550e8400-e29b-41d4-a716-446655441003', 'Minas Gerais', '550e8400-e29b-41d4-a716-446655440001'),
('550e8400-e29b-41d4-a716-446655441004', 'California', '550e8400-e29b-41d4-a716-446655440002'),
('550e8400-e29b-41d4-a716-446655441005', 'Texas', '550e8400-e29b-41d4-a716-446655440002'),
('550e8400-e29b-41d4-a716-446655441006', 'Buenos Aires', '550e8400-e29b-41d4-a716-446655440003'),
('550e8400-e29b-41d4-a716-446655441007', 'Santiago', '550e8400-e29b-41d4-a716-446655440004');

-- Inserindo categorias
INSERT INTO categorias (categoria_id, nome) VALUES
('550e8400-e29b-41d4-a716-446655442001', 'Programação'),
('550e8400-e29b-41d4-a716-446655442002', 'Tecnologia'),
('550e8400-e29b-41d4-a716-446655442003', 'Ficção Científica'),
('550e8400-e29b-41d4-a716-446655442004', 'Literatura'),
('550e8400-e29b-41d4-a716-446655442005', 'Educação'),
('550e8400-e29b-41d4-a716-446655442006', 'Negócios'),
('550e8400-e29b-41d4-a716-446655442007', 'Romance');

-- Inserindo autores
INSERT INTO autores (usuario_id, nome, email, descricao, data_criacao) VALUES
('550e8400-e29b-41d4-a716-446655443001', 'João Silva', 'joao.silva@email.com', 'Especialista em desenvolvimento Java com mais de 10 anos de experiência. Autor de diversos livros sobre programação e arquitetura de software.', '2024-01-15 10:30:00'),
('550e8400-e29b-41d4-a716-446655443002', 'Maria Santos', 'maria.santos@email.com', 'Desenvolvedora senior especializada em tecnologias web e mobile. Palestrante em conferências de tecnologia e mentora de desenvolvedores.', '2024-02-20 14:15:00'),
('550e8400-e29b-41d4-a716-446655443003', 'Pedro Oliveira', 'pedro.oliveira@email.com', 'Arquiteto de software com vasta experiência em microsserviços e cloud computing. Consultor em transformação digital para grandes empresas.', '2024-03-10 09:45:00'),
('550e8400-e29b-41d4-a716-446655443004', 'Ana Costa', 'ana.costa@email.com', 'Escritora e professora universitária com doutorado em Literatura Comparada. Autora de diversos romances premiados e ensaios literários.', '2024-04-05 16:20:00'),
('550e8400-e29b-41d4-a716-446655443005', 'Carlos Ferreira', 'carlos.ferreira@email.com', 'Empreendedor e consultor em estratégia empresarial. MBA pela Harvard Business School e autor de livros sobre inovação e liderança.', '2024-05-12 11:00:00');

-- Inserindo livros
INSERT INTO livros (livro_id, titulo, resumo, sumario, preco, numero_paginas, isbn, data_publicacao, categoria_id, autor_id) VALUES
('550e8400-e29b-41d4-a716-446655444001',
 'Java: Guia Completo para Desenvolvimento',
 'Um guia abrangente sobre desenvolvimento Java, cobrindo desde conceitos básicos até arquiteturas avançadas. Inclui exemplos práticos e projetos reais para consolidar o aprendizado.',
 'Capítulo 1: Introdução ao Java\nCapítulo 2: Orientação a Objetos\nCapítulo 3: Coleções e Streams\nCapítulo 4: Spring Framework\nCapítulo 5: Testes e Qualidade\nCapítulo 6: Deploy e Produção',
 89.90, 450, '978-85-1234-567-8', '2025-12-15 10:00:00',
 '550e8400-e29b-41d4-a716-446655442001', '550e8400-e29b-41d4-a716-446655443001'),

('550e8400-e29b-41d4-a716-446655444002',
 'Desenvolvimento Web Moderno com React',
 'Aprenda a criar aplicações web modernas usando React, incluindo hooks, context API, e integração com APIs REST. Projeto prático incluído.',
 'Capítulo 1: Fundamentos do React\nCapítulo 2: Components e Props\nCapítulo 3: State e Lifecycle\nCapítulo 4: Hooks\nCapítulo 5: Roteamento\nCapítulo 6: Estado Global',
 75.50, 380, '978-85-9876-543-2', '2025-11-20 14:30:00',
 '550e8400-e29b-41d4-a716-446655442001', '550e8400-e29b-41d4-a716-446655443002'),

('550e8400-e29b-41d4-a716-446655444003',
 'Arquitetura de Microsserviços na Prática',
 'Guia prático para implementação de arquiteturas de microsserviços, incluindo padrões, ferramentas e casos de uso reais em ambientes de produção.',
 'Capítulo 1: Conceitos de Microsserviços\nCapítulo 2: Design Patterns\nCapítulo 3: API Gateway\nCapítulo 4: Service Discovery\nCapítulo 5: Monitoramento\nCapítulo 6: Deploy e Escalabilidade',
 120.00, 520, '978-85-5555-111-9', '2026-01-10 16:45:00',
 '550e8400-e29b-41d4-a716-446655442002', '550e8400-e29b-41d4-a716-446655443003'),

('550e8400-e29b-41d4-a716-446655444004',
 'O Último Algoritmo',
 'Romance de ficção científica sobre inteligência artificial e o futuro da humanidade. Uma história envolvente sobre tecnologia, ética e relacionamentos humanos.',
 'Parte 1: O Descobrimento\nParte 2: A Revolução\nParte 3: O Conflito\nParte 4: A Escolha\nEpílogo: Novo Mundo',
 45.90, 320, '978-85-7777-888-3', '2025-12-01 12:00:00',
 '550e8400-e29b-41d4-a716-446655442003', '550e8400-e29b-41d4-a716-446655443004'),

('550e8400-e29b-41d4-a716-446655444005',
 'Liderança e Inovação no Século XXI',
 'Estratégias e práticas para liderar equipes em um mundo em constante transformação. Casos reais de empresas que revolucionaram seus mercados.',
 'Capítulo 1: Liderança Adaptativa\nCapítulo 2: Cultura de Inovação\nCapítulo 3: Transformação Digital\nCapítulo 4: Gestão de Mudanças\nCapítulo 5: Casos de Sucesso',
 65.00, 280, '978-85-2222-333-7', '2026-02-28 09:30:00',
 '550e8400-e29b-41d4-a716-446655442006', '550e8400-e29b-41d4-a716-446655443005'),

('550e8400-e29b-41d4-a716-446655444006',
 'Corações Entrelaçados',
 'Romance contemporâneo sobre dois desenvolvedores que se conhecem em uma startup e vivem uma história de amor entre códigos e deadlines.',
 'Parte 1: O Encontro\nParte 2: A Parceria\nParte 3: Os Obstáculos\nParte 4: A Descoberta\nParte 5: O Final Feliz',
 39.90, 250, '978-85-4444-555-1', '2025-11-30 18:00:00',
 '550e8400-e29b-41d4-a716-446655442007', '550e8400-e29b-41d4-a716-446655443004');
