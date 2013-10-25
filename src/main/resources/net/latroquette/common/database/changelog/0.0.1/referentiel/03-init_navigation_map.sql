--
-- TOC entry 1999 (class 0 OID 49158)
-- Dependencies: 192
-- Data for Name: navigation; Type: TABLE DATA; Schema: public; Owner: latroquette
--

INSERT INTO navigation VALUES (1, NULL, '/index', 'navigation.accueil.label');
INSERT INTO navigation VALUES (2, 1, '/admin/index', 'navigation.admin.board.label');
INSERT INTO navigation VALUES (3, 1, '/profile/index', 'navigation.profile.board.label');
INSERT INTO navigation VALUES (4, 1, '/item/index', 'navigation.item.search.label');
INSERT INTO navigation VALUES (5, 2, '/admin/keywords', 'navigation.admin.keywords.label');
INSERT INTO navigation VALUES (6, 4, '/item/viewItem', 'navigation.item.view.label');
INSERT INTO navigation VALUES (7, 4, '/item/editItem', 'navigation.item.edit.label');
