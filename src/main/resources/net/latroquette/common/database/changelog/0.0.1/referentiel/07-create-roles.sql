--
-- TOC entry 2350 (class 0 OID 122901)
-- Dependencies: 299
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: latroquette
--

INSERT INTO roles VALUES (1, 'admin', 'Y', 'Y', 'Y', 'Y');
INSERT INTO roles VALUES (2, 'user', 'Y', 'N', 'N', 'N');
INSERT INTO roles VALUES (3, 'moderator', 'Y', 'Y', 'Y', 'N');


--
-- TOC entry 2356 (class 0 OID 0)
-- Dependencies: 298
-- Name: roles_role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: latroquette
--

SELECT pg_catalog.setval('roles_role_id_seq', 3, true);