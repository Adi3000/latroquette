--
-- Name: users; Type: TABLE; Schema: public; Owner: latroquette; Tablespace: 
--

CREATE TABLE users (
    user_id serial NOT NULL,
    user_login character varying(50) NOT NULL,
    user_last_ip_login character varying(40),
    user_last_host_name_login character varying(255),
    user_password character varying(32) NOT NULL,
    user_last_date_login timestamp without time zone,
    user_mail character varying(255),
    user_token integer,
    CONSTRAINT users_pkey PRIMARY KEY (user_id),
    CONSTRAINT users_user_login_key UNIQUE (user_login)
);


--
-- TOC entry 172 (class 1259 OID 16417)
-- Name: files; Type: TABLE; Schema: public; Owner: latroquette; Tablespace: 
--

CREATE TABLE files (
    file_id serial NOT NULL,
    file_name character varying(100) NOT NULL,
    file_checksum character(32),
    user_id integer NOT NULL,
    file_garbage_status smallint,
    file_upload_date timestamp without time zone,
    CONSTRAINT files_pkey PRIMARY KEY (file_id),
    CONSTRAINT files_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(user_id)
);



--
-- TOC entry 176 (class 1259 OID 16430)
-- Name: items; Type: TABLE; Schema: public; Owner: latroquette; Tablespace: 
--

CREATE TABLE items (
    item_id serial NOT NULL,
    user_id integer NOT NULL,
    item_status_id integer DEFAULT 0 NOT NULL,
    item_creation_date timestamp without time zone,
    item_update_date timestamp without time zone,
    item_title character varying(255) NOT NULL,
    item_description text,
    keywords_list_id integer,
    CONSTRAINT item_pkey PRIMARY KEY (item_id),
    CONSTRAINT item_users_fkey FOREIGN KEY (user_id) REFERENCES users(user_id)
);


--
-- TOC entry 2356 (class 0 OID 0)
-- Dependencies: 176
-- Name: COLUMN items.user_id; Type: COMMENT; Schema: public; Owner: latroquette
--

COMMENT ON COLUMN items.user_id IS 'Owner_id';


--
-- TOC entry 177 (class 1259 OID 16437)
-- Name: items_files; Type: TABLE; Schema: public; Owner: latroquette; Tablespace: 
--

CREATE TABLE items_files (
    item_id integer NOT NULL,
    file_id integer NOT NULL,
    CONSTRAINT items_files_pkey PRIMARY KEY (item_id, file_id),
    CONSTRAINT items_files_file_id_fkey FOREIGN KEY (file_id) REFERENCES files(file_id),
    CONSTRAINT items_files_item_id_fkey FOREIGN KEY (item_id) REFERENCES items(item_id)
);




--
-- TOC entry 179 (class 1259 OID 16442)
-- Name: keywords; Type: TABLE; Schema: public; Owner: latroquette; Tablespace: 
--

CREATE TABLE keywords (
    keyword_id serial NOT NULL,
    keyword_name character varying(255) NOT NULL,
    keyword_parent_id integer,
    keyword_is_synonym character(1) DEFAULT 'N'::bpchar NOT NULL,
    keyword_in_menu character(1) DEFAULT 'N'::bpchar NOT NULL,
    CONSTRAINT pk_keywords PRIMARY KEY (keyword_id)
);


--
-- TOC entry 181 (class 1259 OID 16449)
-- Name: keywords_relationship; Type: TABLE; Schema: public; Owner: latroquette; Tablespace: 
--

CREATE TABLE keywords_relationship (
    keyword_from_id integer NOT NULL,
    keyword_to_id integer NOT NULL,
    CONSTRAINT keywords_relationship_pkey PRIMARY KEY (keyword_from_id, keyword_to_id),
    CONSTRAINT keywords_relationship_keyword_from_id_fkey FOREIGN KEY (keyword_from_id) REFERENCES keywords(keyword_id)
);



--
-- TOC entry 169 (class 1259 OID 16405)
-- Name: external_keywords; Type: TABLE; Schema: public; Owner: latroquette; Tablespace: 
--

CREATE TABLE external_keywords (
    ext_keyword_id serial NOT NULL,
    source_id character varying(3) NOT NULL,
    ext_uid character varying NOT NULL,
    ext_keyword_name character varying(50),
    ext_keyword_parent_id integer,
    ext_keyword_fullname text,
    ext_keyword_excluded character(1) DEFAULT 'N'::bpchar NOT NULL,
    CONSTRAINT external_keywords_pkey PRIMARY KEY (ext_keyword_id),
    CONSTRAINT external_keywords_ext_keyword_parent_id_fkey FOREIGN KEY (ext_keyword_parent_id) REFERENCES external_keywords(ext_keyword_id),
    CONSTRAINT external_keywords_ext_uid_source_id_key UNIQUE (ext_uid, source_id)
);

CREATE TABLE external_keywords_relationship (
    keyword_id integer NOT NULL,
    ext_keyword_id integer NOT NULL,
    CONSTRAINT external_keywords_relationship_pkey PRIMARY KEY (keyword_id, ext_keyword_id),
    CONSTRAINT external_keywords_relationship_ext_keyword_id_fkey FOREIGN KEY (ext_keyword_id) REFERENCES external_keywords(ext_keyword_id),
    CONSTRAINT external_keywords_relationship_keyword_id_fkey FOREIGN KEY (keyword_id) REFERENCES keywords(keyword_id)
);


--
-- TOC entry 174 (class 1259 OID 16422)
-- Name: geoname_postalcode; Type: TABLE; Schema: public; Owner: latroquette; Tablespace: 
--

CREATE TABLE geoname_postalcode (
    country_code character(2),
    postal_code character varying(20),
    place_name character varying(180),
    admin_name1 character varying(100),
    admin_code1 character varying(20),
    admin_name2 character varying(100),
    admin_code2 character varying(20),
    admin_name3 character varying(100),
    admin_code3 character varying(20),
    latitude numeric,
    longitude numeric,
    accuracy smallint,
    geoname_postalcode_id serial NOT NULL,
    CONSTRAINT geoname_postalcode_pkey PRIMARY KEY (geoname_postalcode_id)
);


--
-- TOC entry 184 (class 1259 OID 16460)
-- Name: location_type; Type: TABLE; Schema: public; Owner: latroquette; Tablespace: 
--

CREATE TABLE location_type (
    location_type_id integer NOT NULL,
    location_type_label character varying(10) NOT NULL,
    CONSTRAINT location_type_pkey PRIMARY KEY (location_type_id)
    
);


--
-- TOC entry 182 (class 1259 OID 16452)
-- Name: locations; Type: TABLE; Schema: public; Owner: latroquette; Tablespace: 
--

CREATE TABLE locations (
    location_id serial NOT NULL,
    location_name character varying(180),
    location_longitude numeric,
    location_latitude numeric,
    location_postal_codes character varying(180),
    location_type_id integer,
    location_id_l1 integer,
    location_id_l2 integer,
    location_id_l3 integer,
    location_id_l4 integer,
    CONSTRAINT location_pkey PRIMARY KEY (location_id),
    CONSTRAINT location_location_type_id_fkey FOREIGN KEY (location_type_id) REFERENCES location_type(location_type_id),
    CONSTRAINT location_location_id_l1_fkey FOREIGN KEY (location_id_l1) REFERENCES locations(location_id)  DEFERRABLE INITIALLY IMMEDIATE,
    CONSTRAINT location_location_id_l2_fkey FOREIGN KEY (location_id_l2) REFERENCES locations(location_id)  DEFERRABLE INITIALLY IMMEDIATE,
    CONSTRAINT location_location_id_l3_fkey FOREIGN KEY (location_id_l3) REFERENCES locations(location_id) DEFERRABLE INITIALLY IMMEDIATE,
    CONSTRAINT location_location_id_l4_fkey FOREIGN KEY (location_id_l4) REFERENCES locations(location_id) DEFERRABLE INITIALLY IMMEDIATE
);


--
-- TOC entry 2359 (class 0 OID 0)
-- Dependencies: 182
-- Name: COLUMN locations.location_postal_codes; Type: COMMENT; Schema: public; Owner: latroquette
--

COMMENT ON COLUMN locations.location_postal_codes IS 'List of postal codes';

--
-- TOC entry 185 (class 1259 OID 16463)
-- Name: parameters; Type: TABLE; Schema: public; Owner: latroquette; Tablespace: 
--

CREATE TABLE parameters (
    param_id serial NOT NULL,
    param_name character varying(20) NOT NULL,
    param_value character varying(255),
    param_data_type character varying(2),
    param_description text,
    CONSTRAINT parameters_pkey PRIMARY KEY (param_id),
    CONSTRAINT param_name_unique UNIQUE (param_name)
);




