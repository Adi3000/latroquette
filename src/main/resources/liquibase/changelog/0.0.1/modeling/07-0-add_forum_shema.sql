--
-- TOC entry 7 (class 2615 OID 17261)
-- Name: forum; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA forum;


SET search_path = forum, pg_catalog;

--
-- TOC entry 683 (class 1247 OID 17262)
-- Dependencies: 7
-- Name: varchar_ci; Type: DOMAIN; Schema: forum; Owner: -
--

CREATE DOMAIN varchar_ci AS character varying(255) NOT NULL DEFAULT ''::character varying;


--
-- TOC entry 291 (class 1255 OID 17263)
-- Dependencies: 7 683 683
-- Name: _varchar_ci_equal(varchar_ci, varchar_ci); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION _varchar_ci_equal(varchar_ci, varchar_ci) RETURNS boolean
    LANGUAGE sql STRICT
    AS $_$SELECT LOWER($1) = LOWER($2)$_$;


--
-- TOC entry 295 (class 1255 OID 17264)
-- Dependencies: 7 683 683
-- Name: _varchar_ci_greater_equals(varchar_ci, varchar_ci); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION _varchar_ci_greater_equals(varchar_ci, varchar_ci) RETURNS boolean
    LANGUAGE sql STRICT
    AS $_$SELECT LOWER($1) >= LOWER($2)$_$;


--
-- TOC entry 304 (class 1255 OID 17265)
-- Dependencies: 683 7 683
-- Name: _varchar_ci_greater_than(varchar_ci, varchar_ci); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION _varchar_ci_greater_than(varchar_ci, varchar_ci) RETURNS boolean
    LANGUAGE sql STRICT
    AS $_$SELECT LOWER($1) > LOWER($2)$_$;


--
-- TOC entry 305 (class 1255 OID 17266)
-- Dependencies: 7 683 683
-- Name: _varchar_ci_less_equal(varchar_ci, varchar_ci); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION _varchar_ci_less_equal(varchar_ci, varchar_ci) RETURNS boolean
    LANGUAGE sql STRICT
    AS $_$SELECT LOWER($1) <= LOWER($2)$_$;


--
-- TOC entry 306 (class 1255 OID 17267)
-- Dependencies: 7 683 683
-- Name: _varchar_ci_less_than(varchar_ci, varchar_ci); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION _varchar_ci_less_than(varchar_ci, varchar_ci) RETURNS boolean
    LANGUAGE sql STRICT
    AS $_$SELECT LOWER($1) < LOWER($2)$_$;


--
-- TOC entry 307 (class 1255 OID 17268)
-- Dependencies: 683 683 7
-- Name: _varchar_ci_not_equal(varchar_ci, varchar_ci); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION _varchar_ci_not_equal(varchar_ci, varchar_ci) RETURNS boolean
    LANGUAGE sql STRICT
    AS $_$SELECT LOWER($1) != LOWER($2)$_$;


--
-- TOC entry 308 (class 1255 OID 18702)
-- Dependencies: 7
-- Name: add_num_text(text, integer); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION add_num_text(text, integer) RETURNS text
    LANGUAGE sql
    AS $_$SELECT CAST ((CAST($1 AS integer) + $2) AS text) AS result$_$;


--
-- TOC entry 309 (class 1255 OID 18703)
-- Dependencies: 7
-- Name: bool_not_eq_int(boolean, integer); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION bool_not_eq_int(boolean, integer) RETURNS boolean
    LANGUAGE sql
    AS $_$SELECT CAST($1 AS integer) != $2 AS result$_$;


--
-- TOC entry 310 (class 1255 OID 18704)
-- Dependencies: 7
-- Name: concat(text, text); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION concat(text, text) RETURNS text
    LANGUAGE sql
    AS $_$SELECT $1 || $2 AS result$_$;


--
-- TOC entry 311 (class 1255 OID 18705)
-- Dependencies: 7
-- Name: date_format(timestamp without time zone, text); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION date_format(timestamp without time zone, text) RETURNS text
    LANGUAGE sql
    AS $_$SELECT
     REPLACE(
         REPLACE($2, '%m', to_char($1, 'MM')),
     '%d', to_char($1, 'DD')) AS result$_$;


--
-- TOC entry 312 (class 1255 OID 18706)
-- Dependencies: 7
-- Name: day(date); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION day(date) RETURNS integer
    LANGUAGE sql
    AS $_$SELECT EXTRACT(DAY FROM DATE($1))::integer AS result$_$;


--
-- TOC entry 313 (class 1255 OID 18707)
-- Dependencies: 7
-- Name: dayofmonth(timestamp without time zone); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION dayofmonth(timestamp without time zone) RETURNS integer
    LANGUAGE sql
    AS $_$SELECT CAST (EXTRACT(DAY FROM $1) AS integer) AS result$_$;


--
-- TOC entry 314 (class 1255 OID 18708)
-- Dependencies: 7
-- Name: find_in_set(text, text); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION find_in_set(needle text, haystack text) RETURNS integer
    LANGUAGE sql
    AS $_$
 	SELECT i AS result
 	FROM generate_series(1, array_upper(string_to_array($2,','), 1)) AS g(i)
 	WHERE  (string_to_array($2,','))[i] = $1
 		UNION ALL
 	SELECT 0
 	LIMIT 1$_$;


--
-- TOC entry 315 (class 1255 OID 18709)
-- Dependencies: 7
-- Name: find_in_set(integer, text); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION find_in_set(needle integer, haystack text) RETURNS integer
    LANGUAGE sql
    AS $_$
 	SELECT i AS result
 	FROM generate_series(1, array_upper(string_to_array($2,','), 1)) AS g(i)
 	WHERE  (string_to_array($2,','))[i] = CAST($1 AS text)
 		UNION ALL
 	SELECT 0
 	LIMIT 1$_$;


--
-- TOC entry 316 (class 1255 OID 18710)
-- Dependencies: 7
-- Name: from_unixtime(integer); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION from_unixtime(integer) RETURNS timestamp without time zone
    LANGUAGE sql
    AS $_$SELECT timestamp 'epoch' + $1 * interval '1 second' AS result$_$;


--
-- TOC entry 317 (class 1255 OID 18711)
-- Dependencies: 7
-- Name: hour(timestamp without time zone); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION hour(timestamp without time zone) RETURNS integer
    LANGUAGE sql
    AS $_$SELECT CAST (EXTRACT(HOUR FROM $1) AS integer) AS result$_$;


--
-- TOC entry 318 (class 1255 OID 18712)
-- Dependencies: 7
-- Name: ifnull(text, text); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION ifnull(text, text) RETURNS text
    LANGUAGE sql
    AS $_$SELECT COALESCE($1, $2) AS result$_$;


--
-- TOC entry 319 (class 1255 OID 18713)
-- Dependencies: 7
-- Name: ifnull(integer, integer); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION ifnull(integer, integer) RETURNS integer
    LANGUAGE sql
    AS $_$SELECT COALESCE($1, $2) AS result$_$;


--
-- TOC entry 320 (class 1255 OID 18714)
-- Dependencies: 7
-- Name: ifnull(bigint, bigint); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION ifnull(bigint, bigint) RETURNS bigint
    LANGUAGE sql
    AS $_$SELECT COALESCE($1, $2) AS result$_$;


--
-- TOC entry 321 (class 1255 OID 18715)
-- Dependencies: 7
-- Name: ifnull(character varying, character varying); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION ifnull(character varying, character varying) RETURNS character varying
    LANGUAGE sql
    AS $_$SELECT COALESCE($1, $2) AS result$_$;


--
-- TOC entry 322 (class 1255 OID 18716)
-- Dependencies: 7
-- Name: ifnull(character varying, boolean); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION ifnull(character varying, boolean) RETURNS character varying
    LANGUAGE sql
    AS $_$SELECT COALESCE($1, CAST(CAST($2 AS int) AS varchar)) AS result$_$;


--
-- TOC entry 323 (class 1255 OID 18717)
-- Dependencies: 7
-- Name: ifnull(integer, boolean); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION ifnull(integer, boolean) RETURNS integer
    LANGUAGE sql
    AS $_$SELECT COALESCE($1, CAST($2 AS int)) AS result$_$;


--
-- TOC entry 324 (class 1255 OID 18718)
-- Dependencies: 7
-- Name: inet_aton(text); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION inet_aton(text) RETURNS bigint
    LANGUAGE sql
    AS $_$SELECT
 	CASE WHEN
 		$1 !~ '^[0-9]?[0-9]?[0-9]?\.[0-9]?[0-9]?[0-9]?\.[0-9]?[0-9]?[0-9]?\.[0-9]?[0-9]?[0-9]?$' THEN 0
 	ELSE
 		split_part($1, '.', 1)::int8 * (256 * 256 * 256) +
 		split_part($1, '.', 2)::int8 * (256 * 256) +
 		split_part($1, '.', 3)::int8 * 256 +
 		split_part($1, '.', 4)::int8
 	END AS result$_$;


--
-- TOC entry 325 (class 1255 OID 18719)
-- Dependencies: 7
-- Name: inet_ntoa(bigint); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION inet_ntoa(bigint) RETURNS text
    LANGUAGE sql
    AS $_$SELECT
     (($1 >> 24) & 255::int8) || '.' ||
     (($1 >> 16) & 255::int8) || '.' ||
     (($1 >> 8) & 255::int8) || '.' ||
     ($1 & 255::int8) AS result$_$;


--
-- TOC entry 326 (class 1255 OID 18720)
-- Dependencies: 7
-- Name: instr(text, text); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION instr(text, text) RETURNS integer
    LANGUAGE sql
    AS $_$SELECT POSITION($2 in $1) AS result$_$;


--
-- TOC entry 327 (class 1255 OID 18721)
-- Dependencies: 7
-- Name: left(text, integer); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION "left"(text, integer) RETURNS text
    LANGUAGE sql
    AS $_$SELECT SUBSTRING($1 FROM 0 FOR $2) AS result$_$;


--
-- TOC entry 328 (class 1255 OID 18722)
-- Dependencies: 7
-- Name: month(timestamp without time zone); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION month(timestamp without time zone) RETURNS integer
    LANGUAGE sql
    AS $_$SELECT CAST (EXTRACT(MONTH FROM $1) AS integer) AS result$_$;


--
-- TOC entry 329 (class 1255 OID 18723)
-- Dependencies: 7
-- Name: to_days(timestamp without time zone); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION to_days(timestamp without time zone) RETURNS integer
    LANGUAGE sql
    AS $_$SELECT DATE_PART('DAY', $1 - '0001-01-01bc')::integer AS result$_$;


--
-- TOC entry 330 (class 1255 OID 18724)
-- Dependencies: 7
-- Name: year(timestamp without time zone); Type: FUNCTION; Schema: forum; Owner: -
--

CREATE FUNCTION year(timestamp without time zone) RETURNS integer
    LANGUAGE sql
    AS $_$SELECT CAST (EXTRACT(YEAR FROM $1) AS integer) AS result$_$;


--
-- TOC entry 1663 (class 2617 OID 18725)
-- Dependencies: 7 308
-- Name: +; Type: OPERATOR; Schema: forum; Owner: -
--

CREATE OPERATOR + (
    PROCEDURE = add_num_text,
    LEFTARG = text,
    RIGHTARG = integer
);


--
-- TOC entry 1657 (class 2617 OID 17271)
-- Dependencies: 7 683 683 306
-- Name: <; Type: OPERATOR; Schema: forum; Owner: -
--

CREATE OPERATOR < (
    PROCEDURE = _varchar_ci_less_than,
    LEFTARG = varchar_ci,
    RIGHTARG = varchar_ci,
    COMMUTATOR = >,
    NEGATOR = >=,
    RESTRICT = scalarltsel,
    JOIN = scalarltjoinsel
);


--
-- TOC entry 1658 (class 2617 OID 17272)
-- Dependencies: 7 683 683 305
-- Name: <=; Type: OPERATOR; Schema: forum; Owner: -
--

CREATE OPERATOR <= (
    PROCEDURE = _varchar_ci_less_equal,
    LEFTARG = varchar_ci,
    RIGHTARG = varchar_ci,
    COMMUTATOR = >=,
    NEGATOR = >,
    RESTRICT = scalarltsel,
    JOIN = scalarltjoinsel
);


--
-- TOC entry 1660 (class 2617 OID 17274)
-- Dependencies: 307 7 683 683
-- Name: <>; Type: OPERATOR; Schema: forum; Owner: -
--

CREATE OPERATOR <> (
    PROCEDURE = _varchar_ci_not_equal,
    LEFTARG = varchar_ci,
    RIGHTARG = varchar_ci,
    COMMUTATOR = <>,
    NEGATOR = =,
    RESTRICT = neqsel,
    JOIN = neqjoinsel
);


--
-- TOC entry 1664 (class 2617 OID 18726)
-- Dependencies: 7 309
-- Name: <>; Type: OPERATOR; Schema: forum; Owner: -
--

CREATE OPERATOR <> (
    PROCEDURE = bool_not_eq_int,
    LEFTARG = boolean,
    RIGHTARG = integer
);


--
-- TOC entry 1662 (class 2617 OID 17273)
-- Dependencies: 291 7 683 683
-- Name: =; Type: OPERATOR; Schema: forum; Owner: -
--

CREATE OPERATOR = (
    PROCEDURE = _varchar_ci_equal,
    LEFTARG = varchar_ci,
    RIGHTARG = varchar_ci,
    COMMUTATOR = =,
    NEGATOR = <>,
    MERGES,
    HASHES,
    RESTRICT = eqsel,
    JOIN = eqjoinsel
);


--
-- TOC entry 1661 (class 2617 OID 17269)
-- Dependencies: 304 683 683 7
-- Name: >; Type: OPERATOR; Schema: forum; Owner: -
--

CREATE OPERATOR > (
    PROCEDURE = _varchar_ci_greater_than,
    LEFTARG = varchar_ci,
    RIGHTARG = varchar_ci,
    COMMUTATOR = <,
    NEGATOR = <=,
    RESTRICT = scalargtsel,
    JOIN = scalargtjoinsel
);


--
-- TOC entry 1659 (class 2617 OID 17270)
-- Dependencies: 683 683 295 7
-- Name: >=; Type: OPERATOR; Schema: forum; Owner: -
--

CREATE OPERATOR >= (
    PROCEDURE = _varchar_ci_greater_equals,
    LEFTARG = varchar_ci,
    RIGHTARG = varchar_ci,
    COMMUTATOR = <=,
    NEGATOR = <,
    RESTRICT = scalargtsel,
    JOIN = scalargtjoinsel
);


--
-- TOC entry 185 (class 1259 OID 18727)
-- Dependencies: 7
-- Name: smf_admin_info_files_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_admin_info_files_seq
    START WITH 8
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


SET default_with_oids = false;

--
-- TOC entry 186 (class 1259 OID 18729)
-- Dependencies: 2323 7
-- Name: smf_admin_info_files; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_admin_info_files (
    id_file smallint DEFAULT nextval('smf_admin_info_files_seq'::regclass) NOT NULL,
    filename character varying(255) NOT NULL,
    path character varying(255) NOT NULL,
    parameters character varying(255) NOT NULL,
    data text NOT NULL,
    filetype character varying(255) NOT NULL
);


--
-- TOC entry 187 (class 1259 OID 18736)
-- Dependencies: 2324 2325 2326 7
-- Name: smf_approval_queue; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_approval_queue (
    id_msg integer DEFAULT 0 NOT NULL,
    id_attach integer DEFAULT 0 NOT NULL,
    id_event smallint DEFAULT (0)::smallint NOT NULL
);


--
-- TOC entry 188 (class 1259 OID 18742)
-- Dependencies: 7
-- Name: smf_attachments_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_attachments_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 189 (class 1259 OID 18744)
-- Dependencies: 2327 2328 2329 2330 2331 2332 2333 2334 2335 2336 2337 2338 2339 2340 7
-- Name: smf_attachments; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_attachments (
    id_attach integer DEFAULT nextval('smf_attachments_seq'::regclass) NOT NULL,
    id_thumb integer DEFAULT 0 NOT NULL,
    id_msg integer DEFAULT 0 NOT NULL,
    id_member integer DEFAULT 0 NOT NULL,
    id_folder smallint DEFAULT (1)::smallint NOT NULL,
    attachment_type smallint DEFAULT (0)::smallint NOT NULL,
    filename character varying(255) NOT NULL,
    file_hash character varying(40) DEFAULT ''::character varying NOT NULL,
    fileext character varying(8) DEFAULT ''::character varying NOT NULL,
    size integer DEFAULT 0 NOT NULL,
    downloads integer DEFAULT 0 NOT NULL,
    width integer DEFAULT 0 NOT NULL,
    height integer DEFAULT 0 NOT NULL,
    mime_type character varying(20) DEFAULT ''::character varying NOT NULL,
    approved smallint DEFAULT (1)::smallint NOT NULL
);


--
-- TOC entry 190 (class 1259 OID 18761)
-- Dependencies: 7
-- Name: smf_ban_groups_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_ban_groups_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 191 (class 1259 OID 18763)
-- Dependencies: 2341 2342 2343 2344 2345 2346 2347 7
-- Name: smf_ban_groups; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_ban_groups (
    id_ban_group integer DEFAULT nextval('smf_ban_groups_seq'::regclass) NOT NULL,
    name character varying(20) DEFAULT ''::character varying NOT NULL,
    ban_time integer DEFAULT 0 NOT NULL,
    expire_time integer,
    cannot_access smallint DEFAULT (0)::smallint NOT NULL,
    cannot_register smallint DEFAULT (0)::smallint NOT NULL,
    cannot_post smallint DEFAULT (0)::smallint NOT NULL,
    cannot_login smallint DEFAULT (0)::smallint NOT NULL,
    reason character varying(255) NOT NULL,
    notes text NOT NULL
);


--
-- TOC entry 192 (class 1259 OID 18776)
-- Dependencies: 7
-- Name: smf_ban_items_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_ban_items_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 193 (class 1259 OID 18778)
-- Dependencies: 2348 2349 2350 2351 2352 2353 2354 2355 2356 2357 2358 2359 7
-- Name: smf_ban_items; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_ban_items (
    id_ban integer DEFAULT nextval('smf_ban_items_seq'::regclass) NOT NULL,
    id_ban_group smallint DEFAULT (0)::smallint NOT NULL,
    ip_low1 smallint DEFAULT (0)::smallint NOT NULL,
    ip_high1 smallint DEFAULT (0)::smallint NOT NULL,
    ip_low2 smallint DEFAULT (0)::smallint NOT NULL,
    ip_high2 smallint DEFAULT (0)::smallint NOT NULL,
    ip_low3 smallint DEFAULT (0)::smallint NOT NULL,
    ip_high3 smallint DEFAULT (0)::smallint NOT NULL,
    ip_low4 smallint DEFAULT (0)::smallint NOT NULL,
    ip_high4 smallint DEFAULT (0)::smallint NOT NULL,
    hostname character varying(255) NOT NULL,
    email_address character varying(255) NOT NULL,
    id_member integer DEFAULT 0 NOT NULL,
    hits integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 194 (class 1259 OID 18796)
-- Dependencies: 2360 2361 2362 2363 7
-- Name: smf_board_permissions; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_board_permissions (
    id_group smallint DEFAULT (0)::smallint NOT NULL,
    id_profile smallint DEFAULT (0)::smallint NOT NULL,
    permission character varying(30) DEFAULT ''::character varying NOT NULL,
    add_deny smallint DEFAULT (1)::smallint NOT NULL
);


--
-- TOC entry 195 (class 1259 OID 18803)
-- Dependencies: 7
-- Name: smf_boards_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_boards_seq
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 196 (class 1259 OID 18805)
-- Dependencies: 2364 2365 2366 2367 2368 2369 2370 2371 2372 2373 2374 2375 2376 2377 2378 2379 2380 7
-- Name: smf_boards; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_boards (
    id_board smallint DEFAULT nextval('smf_boards_seq'::regclass) NOT NULL,
    id_cat smallint DEFAULT (0)::smallint NOT NULL,
    child_level smallint DEFAULT (0)::smallint NOT NULL,
    id_parent smallint DEFAULT (0)::smallint NOT NULL,
    board_order smallint DEFAULT (0)::smallint NOT NULL,
    id_last_msg integer DEFAULT 0 NOT NULL,
    id_msg_updated integer DEFAULT 0 NOT NULL,
    member_groups character varying(255) DEFAULT '-1,0'::character varying NOT NULL,
    id_profile smallint DEFAULT (1)::smallint NOT NULL,
    name character varying(255) NOT NULL,
    description text NOT NULL,
    num_topics integer DEFAULT 0 NOT NULL,
    num_posts integer DEFAULT 0 NOT NULL,
    count_posts smallint DEFAULT (0)::smallint NOT NULL,
    id_theme smallint DEFAULT (0)::smallint NOT NULL,
    override_theme smallint DEFAULT (0)::smallint NOT NULL,
    unapproved_posts smallint DEFAULT (0)::smallint NOT NULL,
    unapproved_topics smallint DEFAULT (0)::smallint NOT NULL,
    redirect character varying(255) DEFAULT ''::character varying NOT NULL
);


--
-- TOC entry 197 (class 1259 OID 18828)
-- Dependencies: 7
-- Name: smf_calendar_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_calendar_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 198 (class 1259 OID 18830)
-- Dependencies: 2381 2382 2383 2384 2385 2386 2387 7
-- Name: smf_calendar; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_calendar (
    id_event smallint DEFAULT nextval('smf_calendar_seq'::regclass) NOT NULL,
    start_date date DEFAULT '0001-01-01'::date NOT NULL,
    end_date date DEFAULT '0001-01-01'::date NOT NULL,
    id_board smallint DEFAULT (0)::smallint NOT NULL,
    id_topic integer DEFAULT 0 NOT NULL,
    title character varying(255) DEFAULT ''::character varying NOT NULL,
    id_member integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 199 (class 1259 OID 18840)
-- Dependencies: 7
-- Name: smf_calendar_holidays_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_calendar_holidays_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 200 (class 1259 OID 18842)
-- Dependencies: 2388 2389 2390 7
-- Name: smf_calendar_holidays; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_calendar_holidays (
    id_holiday smallint DEFAULT nextval('smf_calendar_holidays_seq'::regclass) NOT NULL,
    event_date date DEFAULT '0001-01-01'::date NOT NULL,
    title character varying(255) DEFAULT ''::character varying NOT NULL
);


--
-- TOC entry 201 (class 1259 OID 18848)
-- Dependencies: 7
-- Name: smf_categories_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_categories_seq
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 202 (class 1259 OID 18850)
-- Dependencies: 2391 2392 2393 7
-- Name: smf_categories; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_categories (
    id_cat smallint DEFAULT nextval('smf_categories_seq'::regclass) NOT NULL,
    cat_order smallint DEFAULT (0)::smallint NOT NULL,
    name character varying(255) NOT NULL,
    can_collapse smallint DEFAULT (1)::smallint NOT NULL
);


--
-- TOC entry 203 (class 1259 OID 18856)
-- Dependencies: 2394 2395 7
-- Name: smf_collapsed_categories; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_collapsed_categories (
    id_cat smallint DEFAULT (0)::smallint NOT NULL,
    id_member integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 204 (class 1259 OID 18861)
-- Dependencies: 7
-- Name: smf_custom_fields_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_custom_fields_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 205 (class 1259 OID 18863)
-- Dependencies: 2396 2397 2398 2399 2400 2401 2402 2403 2404 2405 2406 2407 2408 7
-- Name: smf_custom_fields; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_custom_fields (
    id_field smallint DEFAULT nextval('smf_custom_fields_seq'::regclass) NOT NULL,
    col_name character varying(12) DEFAULT ''::character varying NOT NULL,
    field_name character varying(40) DEFAULT ''::character varying NOT NULL,
    field_desc character varying(255) NOT NULL,
    field_type character varying(8) DEFAULT 'text'::character varying NOT NULL,
    field_length smallint DEFAULT (255)::smallint NOT NULL,
    field_options text NOT NULL,
    mask character varying(255) NOT NULL,
    show_reg smallint DEFAULT (0)::smallint NOT NULL,
    show_display smallint DEFAULT (0)::smallint NOT NULL,
    show_profile character varying(20) DEFAULT 'forumprofile'::character varying NOT NULL,
    private smallint DEFAULT (0)::smallint NOT NULL,
    active smallint DEFAULT (1)::smallint NOT NULL,
    bbc smallint DEFAULT (0)::smallint NOT NULL,
    can_search smallint DEFAULT (0)::smallint NOT NULL,
    default_value character varying(255) NOT NULL,
    enclose text NOT NULL,
    placement smallint DEFAULT (0)::smallint NOT NULL
);


--
-- TOC entry 206 (class 1259 OID 18882)
-- Dependencies: 2409 2410 7
-- Name: smf_group_moderators; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_group_moderators (
    id_group smallint DEFAULT (0)::smallint NOT NULL,
    id_member integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 207 (class 1259 OID 18887)
-- Dependencies: 7
-- Name: smf_log_actions_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_log_actions_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 208 (class 1259 OID 18889)
-- Dependencies: 2411 2412 2413 2414 2415 2416 2417 2418 2419 7
-- Name: smf_log_actions; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_actions (
    id_action integer DEFAULT nextval('smf_log_actions_seq'::regclass) NOT NULL,
    id_log smallint DEFAULT (1)::smallint NOT NULL,
    log_time integer DEFAULT 0 NOT NULL,
    id_member integer DEFAULT 0 NOT NULL,
    ip character(16) DEFAULT '                '::bpchar NOT NULL,
    action character varying(30) DEFAULT ''::character varying NOT NULL,
    id_board smallint DEFAULT (0)::smallint NOT NULL,
    id_topic integer DEFAULT 0 NOT NULL,
    id_msg integer DEFAULT 0 NOT NULL,
    extra text NOT NULL
);


--
-- TOC entry 209 (class 1259 OID 18904)
-- Dependencies: 2420 2421 2422 2423 2424 2425 7
-- Name: smf_log_activity; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_activity (
    date date DEFAULT '0001-01-01'::date NOT NULL,
    hits integer DEFAULT 0 NOT NULL,
    topics smallint DEFAULT (0)::smallint NOT NULL,
    posts smallint DEFAULT (0)::smallint NOT NULL,
    registers smallint DEFAULT (0)::smallint NOT NULL,
    most_on smallint DEFAULT (0)::smallint NOT NULL
);


--
-- TOC entry 210 (class 1259 OID 18913)
-- Dependencies: 7
-- Name: smf_log_banned_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_log_banned_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 211 (class 1259 OID 18915)
-- Dependencies: 2426 2427 2428 2429 7
-- Name: smf_log_banned; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_banned (
    id_ban_log integer DEFAULT nextval('smf_log_banned_seq'::regclass) NOT NULL,
    id_member integer DEFAULT 0 NOT NULL,
    ip character(16) DEFAULT '                '::bpchar NOT NULL,
    email character varying(255) NOT NULL,
    log_time integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 212 (class 1259 OID 18922)
-- Dependencies: 2430 2431 2432 7
-- Name: smf_log_boards; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_boards (
    id_member integer DEFAULT 0 NOT NULL,
    id_board smallint DEFAULT (0)::smallint NOT NULL,
    id_msg integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 213 (class 1259 OID 18928)
-- Dependencies: 7
-- Name: smf_log_comments_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_log_comments_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 214 (class 1259 OID 18930)
-- Dependencies: 2433 2434 2435 2436 2437 2438 2439 2440 7
-- Name: smf_log_comments; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_comments (
    id_comment integer DEFAULT nextval('smf_log_comments_seq'::regclass) NOT NULL,
    id_member integer DEFAULT 0 NOT NULL,
    member_name character varying(80) DEFAULT ''::character varying NOT NULL,
    comment_type character varying(8) DEFAULT 'warning'::character varying NOT NULL,
    id_recipient integer DEFAULT 0 NOT NULL,
    recipient_name character varying(255) NOT NULL,
    log_time integer DEFAULT 0 NOT NULL,
    id_notice integer DEFAULT 0 NOT NULL,
    counter smallint DEFAULT (0)::smallint NOT NULL,
    body text NOT NULL
);


--
-- TOC entry 215 (class 1259 OID 18944)
-- Dependencies: 2441 2442 2443 7
-- Name: smf_log_digest; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_digest (
    id_topic integer NOT NULL,
    id_msg integer NOT NULL,
    note_type character varying(10) DEFAULT 'post'::character varying NOT NULL,
    daily smallint DEFAULT (0)::smallint NOT NULL,
    exclude integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 216 (class 1259 OID 18950)
-- Dependencies: 7
-- Name: smf_log_errors_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_log_errors_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 217 (class 1259 OID 18952)
-- Dependencies: 2444 2445 2446 2447 2448 2449 2450 7
-- Name: smf_log_errors; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_errors (
    id_error integer DEFAULT nextval('smf_log_errors_seq'::regclass) NOT NULL,
    log_time integer DEFAULT 0 NOT NULL,
    id_member integer DEFAULT 0 NOT NULL,
    ip character varying(16) DEFAULT ''::character varying NOT NULL,
    url text NOT NULL,
    message text NOT NULL,
    session character(32) DEFAULT '                                '::bpchar NOT NULL,
    error_type character varying(15) DEFAULT 'general'::character varying NOT NULL,
    file character varying(255) NOT NULL,
    line integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 218 (class 1259 OID 18965)
-- Dependencies: 2451 2452 2453 7
-- Name: smf_log_floodcontrol; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_floodcontrol (
    ip character(16) DEFAULT '                '::bpchar NOT NULL,
    log_time integer DEFAULT 0 NOT NULL,
    log_type character varying(8) DEFAULT 'post'::character varying NOT NULL
);


--
-- TOC entry 219 (class 1259 OID 18971)
-- Dependencies: 7
-- Name: smf_log_group_requests_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_log_group_requests_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 220 (class 1259 OID 18973)
-- Dependencies: 2454 2455 2456 2457 7
-- Name: smf_log_group_requests; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_group_requests (
    id_request integer DEFAULT nextval('smf_log_group_requests_seq'::regclass) NOT NULL,
    id_member integer DEFAULT 0 NOT NULL,
    id_group smallint DEFAULT (0)::smallint NOT NULL,
    time_applied integer DEFAULT 0 NOT NULL,
    reason text NOT NULL
);


--
-- TOC entry 221 (class 1259 OID 18983)
-- Dependencies: 2458 2459 2460 2461 7
-- Name: smf_log_karma; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_karma (
    id_target integer DEFAULT 0 NOT NULL,
    id_executor integer DEFAULT 0 NOT NULL,
    log_time integer DEFAULT 0 NOT NULL,
    action smallint DEFAULT (0)::smallint NOT NULL
);


--
-- TOC entry 222 (class 1259 OID 18990)
-- Dependencies: 2462 2463 2464 7
-- Name: smf_log_mark_read; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_mark_read (
    id_member integer DEFAULT 0 NOT NULL,
    id_board smallint DEFAULT (0)::smallint NOT NULL,
    id_msg integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 223 (class 1259 OID 18996)
-- Dependencies: 7
-- Name: smf_log_member_notices_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_log_member_notices_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 224 (class 1259 OID 18998)
-- Dependencies: 2465 7
-- Name: smf_log_member_notices; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_member_notices (
    id_notice integer DEFAULT nextval('smf_log_member_notices_seq'::regclass) NOT NULL,
    subject character varying(255) NOT NULL,
    body text NOT NULL
);


--
-- TOC entry 225 (class 1259 OID 19005)
-- Dependencies: 2466 2467 2468 2469 7
-- Name: smf_log_notify; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_notify (
    id_member integer DEFAULT 0 NOT NULL,
    id_topic integer DEFAULT 0 NOT NULL,
    id_board smallint DEFAULT (0)::smallint NOT NULL,
    sent smallint DEFAULT (0)::smallint NOT NULL
);


--
-- TOC entry 226 (class 1259 OID 19012)
-- Dependencies: 2470 2471 2472 2473 2474 7
-- Name: smf_log_online; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_online (
    session character varying(32) DEFAULT ''::character varying NOT NULL,
    log_time integer DEFAULT 0 NOT NULL,
    id_member integer DEFAULT 0 NOT NULL,
    id_spider smallint DEFAULT (0)::smallint NOT NULL,
    ip bigint DEFAULT (0)::bigint NOT NULL,
    url text NOT NULL
);


--
-- TOC entry 227 (class 1259 OID 19023)
-- Dependencies: 7
-- Name: smf_log_packages_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_log_packages_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 228 (class 1259 OID 19025)
-- Dependencies: 2475 2476 2477 2478 2479 2480 7
-- Name: smf_log_packages; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_packages (
    id_install integer DEFAULT nextval('smf_log_packages_seq'::regclass) NOT NULL,
    filename character varying(255) NOT NULL,
    package_id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    version character varying(255) NOT NULL,
    id_member_installed integer DEFAULT 0 NOT NULL,
    member_installed character varying(255) NOT NULL,
    time_installed integer DEFAULT 0 NOT NULL,
    id_member_removed integer DEFAULT 0 NOT NULL,
    member_removed character varying(255) NOT NULL,
    time_removed integer DEFAULT 0 NOT NULL,
    install_state smallint DEFAULT (1)::smallint NOT NULL,
    failed_steps text NOT NULL,
    themes_installed character varying(255) NOT NULL,
    db_changes text NOT NULL
);


--
-- TOC entry 229 (class 1259 OID 19037)
-- Dependencies: 2481 2482 2483 7
-- Name: smf_log_polls; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_polls (
    id_poll integer DEFAULT 0 NOT NULL,
    id_member integer DEFAULT 0 NOT NULL,
    id_choice smallint DEFAULT (0)::smallint NOT NULL
);


--
-- TOC entry 230 (class 1259 OID 19043)
-- Dependencies: 7
-- Name: smf_log_reported_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_log_reported_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 231 (class 1259 OID 19045)
-- Dependencies: 2484 2485 2486 2487 2488 2489 2490 2491 2492 2493 7
-- Name: smf_log_reported; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_reported (
    id_report integer DEFAULT nextval('smf_log_reported_seq'::regclass) NOT NULL,
    id_msg integer DEFAULT 0 NOT NULL,
    id_topic integer DEFAULT 0 NOT NULL,
    id_board smallint DEFAULT (0)::smallint NOT NULL,
    id_member integer DEFAULT 0 NOT NULL,
    membername character varying(255) NOT NULL,
    subject character varying(255) NOT NULL,
    body text NOT NULL,
    time_started integer DEFAULT 0 NOT NULL,
    time_updated integer DEFAULT 0 NOT NULL,
    num_reports integer DEFAULT 0 NOT NULL,
    closed smallint DEFAULT (0)::smallint NOT NULL,
    ignore_all smallint DEFAULT (0)::smallint NOT NULL
);


--
-- TOC entry 232 (class 1259 OID 19061)
-- Dependencies: 7
-- Name: smf_log_reported_comments_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_log_reported_comments_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 233 (class 1259 OID 19063)
-- Dependencies: 2494 2495 7
-- Name: smf_log_reported_comments; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_reported_comments (
    id_comment integer DEFAULT nextval('smf_log_reported_comments_seq'::regclass) NOT NULL,
    id_report integer DEFAULT 0 NOT NULL,
    id_member integer NOT NULL,
    membername character varying(255) NOT NULL,
    email_address character varying(255) NOT NULL,
    member_ip character varying(255) NOT NULL,
    comment character varying(255) NOT NULL,
    time_sent integer NOT NULL
);


--
-- TOC entry 234 (class 1259 OID 19071)
-- Dependencies: 7
-- Name: smf_log_scheduled_tasks_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_log_scheduled_tasks_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 235 (class 1259 OID 19073)
-- Dependencies: 2496 2497 2498 2499 7
-- Name: smf_log_scheduled_tasks; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_scheduled_tasks (
    id_log integer DEFAULT nextval('smf_log_scheduled_tasks_seq'::regclass) NOT NULL,
    id_task smallint DEFAULT (0)::smallint NOT NULL,
    time_run integer DEFAULT 0 NOT NULL,
    time_taken double precision DEFAULT (0)::double precision NOT NULL
);


--
-- TOC entry 236 (class 1259 OID 19080)
-- Dependencies: 2500 2501 7
-- Name: smf_log_search_messages; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_search_messages (
    id_search smallint DEFAULT (0)::smallint NOT NULL,
    id_msg integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 237 (class 1259 OID 19085)
-- Dependencies: 2502 2503 2504 2505 2506 7
-- Name: smf_log_search_results; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_search_results (
    id_search smallint DEFAULT (0)::smallint NOT NULL,
    id_topic integer DEFAULT 0 NOT NULL,
    id_msg integer DEFAULT 0 NOT NULL,
    relevance smallint DEFAULT (0)::smallint NOT NULL,
    num_matches smallint DEFAULT (0)::smallint NOT NULL
);


--
-- TOC entry 238 (class 1259 OID 19093)
-- Dependencies: 2507 2508 7
-- Name: smf_log_search_subjects; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_search_subjects (
    word character varying(20) DEFAULT ''::character varying NOT NULL,
    id_topic integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 239 (class 1259 OID 19098)
-- Dependencies: 2509 2510 7
-- Name: smf_log_search_topics; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_search_topics (
    id_search smallint DEFAULT (0)::smallint NOT NULL,
    id_topic integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 240 (class 1259 OID 19103)
-- Dependencies: 7
-- Name: smf_log_spider_hits_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_log_spider_hits_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 241 (class 1259 OID 19105)
-- Dependencies: 2511 2512 2513 2514 7
-- Name: smf_log_spider_hits; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_spider_hits (
    id_hit integer DEFAULT nextval('smf_log_spider_hits_seq'::regclass) NOT NULL,
    id_spider smallint DEFAULT (0)::smallint NOT NULL,
    log_time integer DEFAULT 0 NOT NULL,
    url character varying(255) NOT NULL,
    processed smallint DEFAULT (0)::smallint NOT NULL
);


--
-- TOC entry 242 (class 1259 OID 19112)
-- Dependencies: 2515 2516 2517 2518 7
-- Name: smf_log_spider_stats; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_spider_stats (
    id_spider smallint DEFAULT (0)::smallint NOT NULL,
    page_hits smallint DEFAULT (0)::smallint NOT NULL,
    last_seen integer DEFAULT 0 NOT NULL,
    stat_date date DEFAULT '0001-01-01'::date NOT NULL
);


--
-- TOC entry 243 (class 1259 OID 19119)
-- Dependencies: 7
-- Name: smf_log_subscribed_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_log_subscribed_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 244 (class 1259 OID 19121)
-- Dependencies: 2519 2520 2521 2522 2523 2524 2525 2526 2527 2528 2529 7
-- Name: smf_log_subscribed; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_subscribed (
    id_sublog integer DEFAULT nextval('smf_log_subscribed_seq'::regclass) NOT NULL,
    id_subscribe smallint DEFAULT (0)::smallint NOT NULL,
    id_member integer DEFAULT 0 NOT NULL,
    old_id_group integer DEFAULT 0 NOT NULL,
    start_time integer DEFAULT 0 NOT NULL,
    end_time integer DEFAULT 0 NOT NULL,
    payments_pending smallint DEFAULT (0)::smallint NOT NULL,
    status smallint DEFAULT (0)::smallint NOT NULL,
    pending_details text DEFAULT ''::text NOT NULL,
    reminder_sent smallint DEFAULT (0)::smallint NOT NULL,
    vendor_ref character varying(255) DEFAULT ''::character varying NOT NULL
);


--
-- TOC entry 245 (class 1259 OID 19138)
-- Dependencies: 2530 2531 2532 7
-- Name: smf_log_topics; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_log_topics (
    id_member integer DEFAULT 0 NOT NULL,
    id_topic integer DEFAULT 0 NOT NULL,
    id_msg integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 246 (class 1259 OID 19144)
-- Dependencies: 7
-- Name: smf_mail_queue_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_mail_queue_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 247 (class 1259 OID 19146)
-- Dependencies: 2533 2534 2535 2536 2537 7
-- Name: smf_mail_queue; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_mail_queue (
    id_mail integer DEFAULT nextval('smf_mail_queue_seq'::regclass) NOT NULL,
    time_sent integer DEFAULT 0 NOT NULL,
    recipient character varying(255) NOT NULL,
    body text NOT NULL,
    subject character varying(255) NOT NULL,
    headers text NOT NULL,
    send_html smallint DEFAULT (0)::smallint NOT NULL,
    priority smallint DEFAULT (1)::smallint NOT NULL,
    private smallint DEFAULT (0)::smallint NOT NULL
);


--
-- TOC entry 248 (class 1259 OID 19157)
-- Dependencies: 7
-- Name: smf_membergroups_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_membergroups_seq
    START WITH 9
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 249 (class 1259 OID 19159)
-- Dependencies: 2538 2539 2540 2541 2542 2543 2544 2545 7
-- Name: smf_membergroups; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_membergroups (
    id_group smallint DEFAULT nextval('smf_membergroups_seq'::regclass) NOT NULL,
    group_name character varying(80) DEFAULT ''::character varying NOT NULL,
    description text NOT NULL,
    online_color character varying(20) DEFAULT ''::character varying NOT NULL,
    min_posts integer DEFAULT (-1) NOT NULL,
    max_messages smallint DEFAULT (0)::smallint NOT NULL,
    stars character varying(255) NOT NULL,
    group_type smallint DEFAULT (0)::smallint NOT NULL,
    hidden smallint DEFAULT (0)::smallint NOT NULL,
    id_parent smallint DEFAULT ((-2))::smallint NOT NULL
);


--
-- TOC entry 250 (class 1259 OID 19173)
-- Dependencies: 7
-- Name: smf_members_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_members_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 251 (class 1259 OID 19175)
-- Dependencies: 2546 2547 2548 2549 2550 2551 2552 2553 2554 2555 2556 2557 2558 2559 2560 2561 2562 2563 2564 2565 2566 2567 2568 2569 2570 2571 2572 2573 2574 2575 2576 2577 2578 2579 2580 2581 2582 2583 2584 7
-- Name: smf_members; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_members (
    id_member integer DEFAULT nextval('smf_members_seq'::regclass) NOT NULL,
    member_name character varying(80) DEFAULT ''::character varying NOT NULL,
    date_registered integer DEFAULT 0 NOT NULL,
    posts integer DEFAULT 0 NOT NULL,
    id_group smallint DEFAULT (0)::smallint NOT NULL,
    lngfile character varying(255) NOT NULL,
    last_login integer DEFAULT 0 NOT NULL,
    real_name character varying(255) NOT NULL,
    instant_messages smallint DEFAULT 0 NOT NULL,
    unread_messages smallint DEFAULT 0 NOT NULL,
    new_pm smallint DEFAULT (0)::smallint NOT NULL,
    buddy_list text NOT NULL,
    pm_ignore_list character varying(255) NOT NULL,
    pm_prefs integer DEFAULT 0 NOT NULL,
    mod_prefs character varying(20) DEFAULT ''::character varying NOT NULL,
    message_labels text NOT NULL,
    passwd character varying(64) DEFAULT ''::character varying NOT NULL,
    openid_uri text NOT NULL,
    email_address character varying(255) NOT NULL,
    personal_text character varying(255) NOT NULL,
    gender smallint DEFAULT (0)::smallint NOT NULL,
    birthdate date DEFAULT '0001-01-01'::date NOT NULL,
    website_title character varying(255) NOT NULL,
    website_url character varying(255) NOT NULL,
    location character varying(255) NOT NULL,
    icq character varying(255) NOT NULL,
    aim character varying(255) DEFAULT ''::character varying NOT NULL,
    yim character varying(32) DEFAULT ''::character varying NOT NULL,
    msn character varying(255) NOT NULL,
    hide_email smallint DEFAULT (0)::smallint NOT NULL,
    show_online smallint DEFAULT (1)::smallint NOT NULL,
    time_format character varying(80) DEFAULT ''::character varying NOT NULL,
    signature text NOT NULL,
    time_offset double precision DEFAULT (0)::double precision NOT NULL,
    avatar character varying(255) NOT NULL,
    pm_email_notify smallint DEFAULT (0)::smallint NOT NULL,
    karma_bad smallint DEFAULT (0)::smallint NOT NULL,
    karma_good smallint DEFAULT (0)::smallint NOT NULL,
    usertitle character varying(255) NOT NULL,
    notify_announcements smallint DEFAULT (1)::smallint NOT NULL,
    notify_regularity smallint DEFAULT (1)::smallint NOT NULL,
    notify_send_body smallint DEFAULT (0)::smallint NOT NULL,
    notify_types smallint DEFAULT (2)::smallint NOT NULL,
    member_ip character varying(255) NOT NULL,
    member_ip2 character varying(255) NOT NULL,
    secret_question character varying(255) NOT NULL,
    secret_answer character varying(64) DEFAULT ''::character varying NOT NULL,
    id_theme smallint DEFAULT (0)::smallint NOT NULL,
    is_activated smallint DEFAULT (1)::smallint NOT NULL,
    validation_code character varying(10) DEFAULT ''::character varying NOT NULL,
    id_msg_last_visit integer DEFAULT 0 NOT NULL,
    additional_groups character varying(255) NOT NULL,
    smiley_set character varying(48) DEFAULT ''::character varying NOT NULL,
    id_post_group smallint DEFAULT (0)::smallint NOT NULL,
    total_time_logged_in integer DEFAULT 0 NOT NULL,
    password_salt character varying(255) DEFAULT ''::character varying NOT NULL,
    ignore_boards text NOT NULL,
    warning smallint DEFAULT (0)::smallint NOT NULL,
    passwd_flood character varying(12) DEFAULT ''::character varying NOT NULL,
    pm_receive_from smallint DEFAULT (1)::smallint NOT NULL
);


--
-- TOC entry 252 (class 1259 OID 19220)
-- Dependencies: 7
-- Name: smf_message_icons_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_message_icons_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 253 (class 1259 OID 19222)
-- Dependencies: 2585 2586 2587 2588 2589 7
-- Name: smf_message_icons; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_message_icons (
    id_icon smallint DEFAULT nextval('smf_message_icons_seq'::regclass) NOT NULL,
    title character varying(80) DEFAULT ''::character varying NOT NULL,
    filename character varying(80) DEFAULT ''::character varying NOT NULL,
    id_board smallint DEFAULT (0)::smallint NOT NULL,
    icon_order smallint DEFAULT (0)::smallint NOT NULL
);


--
-- TOC entry 254 (class 1259 OID 19230)
-- Dependencies: 7
-- Name: smf_messages_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_messages_seq
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 255 (class 1259 OID 19232)
-- Dependencies: 2590 2591 2592 2593 2594 2595 2596 2597 2598 2599 7
-- Name: smf_messages; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_messages (
    id_msg integer DEFAULT nextval('smf_messages_seq'::regclass) NOT NULL,
    id_topic integer DEFAULT 0 NOT NULL,
    id_board smallint DEFAULT (0)::smallint NOT NULL,
    poster_time integer DEFAULT 0 NOT NULL,
    id_member integer DEFAULT 0 NOT NULL,
    id_msg_modified integer DEFAULT 0 NOT NULL,
    subject character varying(255) NOT NULL,
    poster_name character varying(255) NOT NULL,
    poster_email character varying(255) NOT NULL,
    poster_ip character varying(255) NOT NULL,
    smileys_enabled smallint DEFAULT (1)::smallint NOT NULL,
    modified_time integer DEFAULT 0 NOT NULL,
    modified_name character varying(255) NOT NULL,
    body text NOT NULL,
    icon character varying(16) DEFAULT 'xx'::character varying NOT NULL,
    approved smallint DEFAULT (1)::smallint NOT NULL
);


--
-- TOC entry 256 (class 1259 OID 19248)
-- Dependencies: 2600 2601 7
-- Name: smf_moderators; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_moderators (
    id_board smallint DEFAULT (0)::smallint NOT NULL,
    id_member integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 257 (class 1259 OID 19253)
-- Dependencies: 2602 2603 7
-- Name: smf_openid_assoc; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_openid_assoc (
    server_url text NOT NULL,
    handle character varying(255) NOT NULL,
    secret text NOT NULL,
    issued integer DEFAULT 0 NOT NULL,
    expires integer DEFAULT 0 NOT NULL,
    assoc_type character varying(64) NOT NULL
);


--
-- TOC entry 258 (class 1259 OID 19261)
-- Dependencies: 7
-- Name: smf_package_servers_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_package_servers_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 259 (class 1259 OID 19263)
-- Dependencies: 2604 7
-- Name: smf_package_servers; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_package_servers (
    id_server smallint DEFAULT nextval('smf_package_servers_seq'::regclass) NOT NULL,
    name character varying(255) NOT NULL,
    url character varying(255) NOT NULL
);


--
-- TOC entry 260 (class 1259 OID 19270)
-- Dependencies: 7
-- Name: smf_permission_profiles_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_permission_profiles_seq
    START WITH 5
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 261 (class 1259 OID 19272)
-- Dependencies: 2605 7
-- Name: smf_permission_profiles; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_permission_profiles (
    id_profile smallint DEFAULT nextval('smf_permission_profiles_seq'::regclass) NOT NULL,
    profile_name character varying(255) NOT NULL
);


--
-- TOC entry 262 (class 1259 OID 19276)
-- Dependencies: 2606 2607 2608 7
-- Name: smf_permissions; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_permissions (
    id_group smallint DEFAULT (0)::smallint NOT NULL,
    permission character varying(30) DEFAULT ''::character varying NOT NULL,
    add_deny smallint DEFAULT (1)::smallint NOT NULL
);


--
-- TOC entry 263 (class 1259 OID 19282)
-- Dependencies: 7
-- Name: smf_personal_messages_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_personal_messages_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 264 (class 1259 OID 19284)
-- Dependencies: 2609 2610 2611 2612 2613 7
-- Name: smf_personal_messages; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_personal_messages (
    id_pm integer DEFAULT nextval('smf_personal_messages_seq'::regclass) NOT NULL,
    id_pm_head integer DEFAULT 0 NOT NULL,
    id_member_from integer DEFAULT 0 NOT NULL,
    deleted_by_sender smallint DEFAULT (0)::smallint NOT NULL,
    from_name character varying(255) NOT NULL,
    msgtime integer DEFAULT 0 NOT NULL,
    subject character varying(255) NOT NULL,
    body text NOT NULL
);


--
-- TOC entry 265 (class 1259 OID 19295)
-- Dependencies: 2614 2615 2616 2617 2618 2619 2620 7
-- Name: smf_pm_recipients; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_pm_recipients (
    id_pm integer DEFAULT 0 NOT NULL,
    id_member integer DEFAULT 0 NOT NULL,
    labels character varying(60) DEFAULT '-1'::character varying NOT NULL,
    bcc smallint DEFAULT (0)::smallint NOT NULL,
    is_read smallint DEFAULT (0)::smallint NOT NULL,
    is_new smallint DEFAULT (0)::smallint NOT NULL,
    deleted smallint DEFAULT (0)::smallint NOT NULL
);


--
-- TOC entry 266 (class 1259 OID 19305)
-- Dependencies: 7
-- Name: smf_pm_rules_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_pm_rules_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 267 (class 1259 OID 19307)
-- Dependencies: 2621 2622 2623 2624 7
-- Name: smf_pm_rules; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_pm_rules (
    id_rule integer DEFAULT nextval('smf_pm_rules_seq'::regclass) NOT NULL,
    id_member integer DEFAULT 0 NOT NULL,
    rule_name character varying(60) NOT NULL,
    criteria text NOT NULL,
    actions text NOT NULL,
    delete_pm smallint DEFAULT (0)::smallint NOT NULL,
    is_or smallint DEFAULT (0)::smallint NOT NULL
);


--
-- TOC entry 268 (class 1259 OID 19317)
-- Dependencies: 2625 2626 2627 7
-- Name: smf_poll_choices; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_poll_choices (
    id_poll integer DEFAULT 0 NOT NULL,
    id_choice smallint DEFAULT (0)::smallint NOT NULL,
    label character varying(255) NOT NULL,
    votes smallint DEFAULT (0)::smallint NOT NULL
);


--
-- TOC entry 269 (class 1259 OID 19323)
-- Dependencies: 7
-- Name: smf_polls_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_polls_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 270 (class 1259 OID 19325)
-- Dependencies: 2628 2629 2630 2631 2632 2633 2634 2635 2636 2637 7
-- Name: smf_polls; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_polls (
    id_poll integer DEFAULT nextval('smf_polls_seq'::regclass) NOT NULL,
    question character varying(255) NOT NULL,
    voting_locked smallint DEFAULT (0)::smallint NOT NULL,
    max_votes smallint DEFAULT (1)::smallint NOT NULL,
    expire_time integer DEFAULT 0 NOT NULL,
    hide_results smallint DEFAULT (0)::smallint NOT NULL,
    change_vote smallint DEFAULT (0)::smallint NOT NULL,
    guest_vote smallint DEFAULT (0)::smallint NOT NULL,
    num_guest_voters integer DEFAULT 0 NOT NULL,
    reset_poll integer DEFAULT 0 NOT NULL,
    id_member integer DEFAULT 0 NOT NULL,
    poster_name character varying(255) NOT NULL
);


--
-- TOC entry 271 (class 1259 OID 19341)
-- Dependencies: 7
-- Name: smf_scheduled_tasks_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_scheduled_tasks_seq
    START WITH 9
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 272 (class 1259 OID 19343)
-- Dependencies: 2638 2639 2640 2641 2642 2643 2644 7
-- Name: smf_scheduled_tasks; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_scheduled_tasks (
    id_task smallint DEFAULT nextval('smf_scheduled_tasks_seq'::regclass) NOT NULL,
    next_time integer DEFAULT 0 NOT NULL,
    time_offset integer DEFAULT 0 NOT NULL,
    time_regularity smallint DEFAULT (0)::smallint NOT NULL,
    time_unit character varying(1) DEFAULT 'h'::character varying NOT NULL,
    disabled smallint DEFAULT (0)::smallint NOT NULL,
    task character varying(24) DEFAULT ''::character varying NOT NULL
);


--
-- TOC entry 273 (class 1259 OID 19353)
-- Dependencies: 7
-- Name: smf_sessions; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_sessions (
    session_id character(32) NOT NULL,
    last_update integer NOT NULL,
    data text NOT NULL
);


--
-- TOC entry 274 (class 1259 OID 19359)
-- Dependencies: 7
-- Name: smf_settings; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_settings (
    variable character varying(255) NOT NULL,
    value text NOT NULL
);


--
-- TOC entry 275 (class 1259 OID 19365)
-- Dependencies: 7
-- Name: smf_smileys_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_smileys_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 276 (class 1259 OID 19367)
-- Dependencies: 2645 2646 2647 2648 2649 2650 2651 7
-- Name: smf_smileys; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_smileys (
    id_smiley smallint DEFAULT nextval('smf_smileys_seq'::regclass) NOT NULL,
    code character varying(30) DEFAULT ''::character varying NOT NULL,
    filename character varying(48) DEFAULT ''::character varying NOT NULL,
    description character varying(80) DEFAULT ''::character varying NOT NULL,
    smiley_row smallint DEFAULT (0)::smallint NOT NULL,
    smiley_order smallint DEFAULT (0)::smallint NOT NULL,
    hidden smallint DEFAULT (0)::smallint NOT NULL
);


--
-- TOC entry 277 (class 1259 OID 19377)
-- Dependencies: 7
-- Name: smf_spiders_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_spiders_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 278 (class 1259 OID 19379)
-- Dependencies: 2652 7
-- Name: smf_spiders; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_spiders (
    id_spider smallint DEFAULT nextval('smf_spiders_seq'::regclass) NOT NULL,
    spider_name character varying(255) NOT NULL,
    user_agent character varying(255) NOT NULL,
    ip_info character varying(255) NOT NULL
);


--
-- TOC entry 279 (class 1259 OID 19386)
-- Dependencies: 7
-- Name: smf_subscriptions_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_subscriptions_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 280 (class 1259 OID 19388)
-- Dependencies: 2653 2654 2655 2656 2657 2658 7
-- Name: smf_subscriptions; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_subscriptions (
    id_subscribe smallint DEFAULT nextval('smf_subscriptions_seq'::regclass) NOT NULL,
    name character varying(60) NOT NULL,
    description character varying(255) NOT NULL,
    cost text NOT NULL,
    length character varying(6) NOT NULL,
    id_group integer DEFAULT 0 NOT NULL,
    add_groups character varying(40) NOT NULL,
    active smallint DEFAULT (1)::smallint NOT NULL,
    repeatable smallint DEFAULT (0)::smallint NOT NULL,
    allow_partial smallint DEFAULT (0)::smallint NOT NULL,
    reminder smallint DEFAULT (0)::smallint NOT NULL,
    email_complete text NOT NULL
);


--
-- TOC entry 281 (class 1259 OID 19400)
-- Dependencies: 2659 2660 7
-- Name: smf_themes; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_themes (
    id_member integer DEFAULT 0 NOT NULL,
    id_theme smallint DEFAULT (1)::smallint NOT NULL,
    variable character varying(255) NOT NULL,
    value text NOT NULL
);


--
-- TOC entry 282 (class 1259 OID 19408)
-- Dependencies: 7
-- Name: smf_topics_seq; Type: SEQUENCE; Schema: forum; Owner: -
--

CREATE SEQUENCE smf_topics_seq
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 283 (class 1259 OID 19410)
-- Dependencies: 2661 2662 2663 2664 2665 2666 2667 2668 2669 2670 2671 2672 2673 2674 2675 7
-- Name: smf_topics; Type: TABLE; Schema: forum; Owner: -
--

CREATE TABLE smf_topics (
    id_topic integer DEFAULT nextval('smf_topics_seq'::regclass) NOT NULL,
    is_sticky smallint DEFAULT (0)::smallint NOT NULL,
    id_board smallint DEFAULT (0)::smallint NOT NULL,
    id_first_msg integer DEFAULT 0 NOT NULL,
    id_last_msg integer DEFAULT 0 NOT NULL,
    id_member_started integer DEFAULT 0 NOT NULL,
    id_member_updated integer DEFAULT 0 NOT NULL,
    id_poll integer DEFAULT 0 NOT NULL,
    id_previous_board smallint DEFAULT (0)::smallint NOT NULL,
    id_previous_topic integer DEFAULT 0 NOT NULL,
    num_replies integer DEFAULT 0 NOT NULL,
    num_views integer DEFAULT 0 NOT NULL,
    locked smallint DEFAULT (0)::smallint NOT NULL,
    unapproved_posts smallint DEFAULT (0)::smallint NOT NULL,
    approved smallint DEFAULT (1)::smallint NOT NULL
);


--
-- TOC entry 2677 (class 2606 OID 19433)
-- Dependencies: 186 186 2797
-- Name: smf_admin_info_files_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_admin_info_files
    ADD CONSTRAINT smf_admin_info_files_pkey PRIMARY KEY (id_file);


--
-- TOC entry 2679 (class 2606 OID 19435)
-- Dependencies: 189 189 2797
-- Name: smf_attachments_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_attachments
    ADD CONSTRAINT smf_attachments_pkey PRIMARY KEY (id_attach);


--
-- TOC entry 2681 (class 2606 OID 19437)
-- Dependencies: 191 191 2797
-- Name: smf_ban_groups_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_ban_groups
    ADD CONSTRAINT smf_ban_groups_pkey PRIMARY KEY (id_ban_group);


--
-- TOC entry 2683 (class 2606 OID 19439)
-- Dependencies: 193 193 2797
-- Name: smf_ban_items_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_ban_items
    ADD CONSTRAINT smf_ban_items_pkey PRIMARY KEY (id_ban);


--
-- TOC entry 2685 (class 2606 OID 19441)
-- Dependencies: 194 194 194 194 2797
-- Name: smf_board_permissions_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_board_permissions
    ADD CONSTRAINT smf_board_permissions_pkey PRIMARY KEY (id_group, id_profile, permission);


--
-- TOC entry 2687 (class 2606 OID 19443)
-- Dependencies: 196 196 2797
-- Name: smf_boards_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_boards
    ADD CONSTRAINT smf_boards_pkey PRIMARY KEY (id_board);


--
-- TOC entry 2691 (class 2606 OID 19445)
-- Dependencies: 200 200 2797
-- Name: smf_calendar_holidays_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_calendar_holidays
    ADD CONSTRAINT smf_calendar_holidays_pkey PRIMARY KEY (id_holiday);


--
-- TOC entry 2689 (class 2606 OID 19447)
-- Dependencies: 198 198 2797
-- Name: smf_calendar_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_calendar
    ADD CONSTRAINT smf_calendar_pkey PRIMARY KEY (id_event);


--
-- TOC entry 2693 (class 2606 OID 19449)
-- Dependencies: 202 202 2797
-- Name: smf_categories_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_categories
    ADD CONSTRAINT smf_categories_pkey PRIMARY KEY (id_cat);


--
-- TOC entry 2695 (class 2606 OID 19451)
-- Dependencies: 203 203 203 2797
-- Name: smf_collapsed_categories_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_collapsed_categories
    ADD CONSTRAINT smf_collapsed_categories_pkey PRIMARY KEY (id_cat, id_member);


--
-- TOC entry 2697 (class 2606 OID 19453)
-- Dependencies: 205 205 2797
-- Name: smf_custom_fields_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_custom_fields
    ADD CONSTRAINT smf_custom_fields_pkey PRIMARY KEY (id_field);


--
-- TOC entry 2699 (class 2606 OID 19455)
-- Dependencies: 206 206 206 2797
-- Name: smf_group_moderators_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_group_moderators
    ADD CONSTRAINT smf_group_moderators_pkey PRIMARY KEY (id_group, id_member);


--
-- TOC entry 2701 (class 2606 OID 19457)
-- Dependencies: 208 208 2797
-- Name: smf_log_actions_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_actions
    ADD CONSTRAINT smf_log_actions_pkey PRIMARY KEY (id_action);


--
-- TOC entry 2703 (class 2606 OID 19459)
-- Dependencies: 209 209 2797
-- Name: smf_log_activity_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_activity
    ADD CONSTRAINT smf_log_activity_pkey PRIMARY KEY (date);


--
-- TOC entry 2705 (class 2606 OID 19461)
-- Dependencies: 211 211 2797
-- Name: smf_log_banned_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_banned
    ADD CONSTRAINT smf_log_banned_pkey PRIMARY KEY (id_ban_log);


--
-- TOC entry 2707 (class 2606 OID 19463)
-- Dependencies: 212 212 212 2797
-- Name: smf_log_boards_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_boards
    ADD CONSTRAINT smf_log_boards_pkey PRIMARY KEY (id_member, id_board);


--
-- TOC entry 2709 (class 2606 OID 19465)
-- Dependencies: 214 214 2797
-- Name: smf_log_comments_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_comments
    ADD CONSTRAINT smf_log_comments_pkey PRIMARY KEY (id_comment);


--
-- TOC entry 2711 (class 2606 OID 19467)
-- Dependencies: 217 217 2797
-- Name: smf_log_errors_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_errors
    ADD CONSTRAINT smf_log_errors_pkey PRIMARY KEY (id_error);


--
-- TOC entry 2713 (class 2606 OID 19469)
-- Dependencies: 218 218 218 2797
-- Name: smf_log_floodcontrol_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_floodcontrol
    ADD CONSTRAINT smf_log_floodcontrol_pkey PRIMARY KEY (ip, log_type);


--
-- TOC entry 2715 (class 2606 OID 19471)
-- Dependencies: 220 220 2797
-- Name: smf_log_group_requests_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_group_requests
    ADD CONSTRAINT smf_log_group_requests_pkey PRIMARY KEY (id_request);


--
-- TOC entry 2717 (class 2606 OID 19473)
-- Dependencies: 221 221 221 2797
-- Name: smf_log_karma_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_karma
    ADD CONSTRAINT smf_log_karma_pkey PRIMARY KEY (id_target, id_executor);


--
-- TOC entry 2719 (class 2606 OID 19475)
-- Dependencies: 222 222 222 2797
-- Name: smf_log_mark_read_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_mark_read
    ADD CONSTRAINT smf_log_mark_read_pkey PRIMARY KEY (id_member, id_board);


--
-- TOC entry 2721 (class 2606 OID 19477)
-- Dependencies: 224 224 2797
-- Name: smf_log_member_notices_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_member_notices
    ADD CONSTRAINT smf_log_member_notices_pkey PRIMARY KEY (id_notice);


--
-- TOC entry 2723 (class 2606 OID 19479)
-- Dependencies: 225 225 225 225 2797
-- Name: smf_log_notify_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_notify
    ADD CONSTRAINT smf_log_notify_pkey PRIMARY KEY (id_member, id_topic, id_board);


--
-- TOC entry 2725 (class 2606 OID 19481)
-- Dependencies: 226 226 2797
-- Name: smf_log_online_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_online
    ADD CONSTRAINT smf_log_online_pkey PRIMARY KEY (session);


--
-- TOC entry 2727 (class 2606 OID 19483)
-- Dependencies: 228 228 2797
-- Name: smf_log_packages_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_packages
    ADD CONSTRAINT smf_log_packages_pkey PRIMARY KEY (id_install);


--
-- TOC entry 2731 (class 2606 OID 19485)
-- Dependencies: 233 233 2797
-- Name: smf_log_reported_comments_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_reported_comments
    ADD CONSTRAINT smf_log_reported_comments_pkey PRIMARY KEY (id_comment);


--
-- TOC entry 2729 (class 2606 OID 19487)
-- Dependencies: 231 231 2797
-- Name: smf_log_reported_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_reported
    ADD CONSTRAINT smf_log_reported_pkey PRIMARY KEY (id_report);


--
-- TOC entry 2733 (class 2606 OID 19489)
-- Dependencies: 235 235 2797
-- Name: smf_log_scheduled_tasks_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_scheduled_tasks
    ADD CONSTRAINT smf_log_scheduled_tasks_pkey PRIMARY KEY (id_log);


--
-- TOC entry 2735 (class 2606 OID 19491)
-- Dependencies: 236 236 236 2797
-- Name: smf_log_search_messages_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_search_messages
    ADD CONSTRAINT smf_log_search_messages_pkey PRIMARY KEY (id_search, id_msg);


--
-- TOC entry 2737 (class 2606 OID 19493)
-- Dependencies: 237 237 237 2797
-- Name: smf_log_search_results_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_search_results
    ADD CONSTRAINT smf_log_search_results_pkey PRIMARY KEY (id_search, id_topic);


--
-- TOC entry 2739 (class 2606 OID 19495)
-- Dependencies: 238 238 238 2797
-- Name: smf_log_search_subjects_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_search_subjects
    ADD CONSTRAINT smf_log_search_subjects_pkey PRIMARY KEY (word, id_topic);


--
-- TOC entry 2741 (class 2606 OID 19497)
-- Dependencies: 239 239 239 2797
-- Name: smf_log_search_topics_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_search_topics
    ADD CONSTRAINT smf_log_search_topics_pkey PRIMARY KEY (id_search, id_topic);


--
-- TOC entry 2743 (class 2606 OID 19499)
-- Dependencies: 241 241 2797
-- Name: smf_log_spider_hits_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_spider_hits
    ADD CONSTRAINT smf_log_spider_hits_pkey PRIMARY KEY (id_hit);


--
-- TOC entry 2745 (class 2606 OID 19501)
-- Dependencies: 242 242 242 2797
-- Name: smf_log_spider_stats_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_spider_stats
    ADD CONSTRAINT smf_log_spider_stats_pkey PRIMARY KEY (stat_date, id_spider);


--
-- TOC entry 2747 (class 2606 OID 19503)
-- Dependencies: 244 244 2797
-- Name: smf_log_subscribed_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_subscribed
    ADD CONSTRAINT smf_log_subscribed_pkey PRIMARY KEY (id_sublog);


--
-- TOC entry 2749 (class 2606 OID 19505)
-- Dependencies: 245 245 245 2797
-- Name: smf_log_topics_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_log_topics
    ADD CONSTRAINT smf_log_topics_pkey PRIMARY KEY (id_member, id_topic);


--
-- TOC entry 2751 (class 2606 OID 19507)
-- Dependencies: 247 247 2797
-- Name: smf_mail_queue_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_mail_queue
    ADD CONSTRAINT smf_mail_queue_pkey PRIMARY KEY (id_mail);


--
-- TOC entry 2753 (class 2606 OID 19509)
-- Dependencies: 249 249 2797
-- Name: smf_membergroups_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_membergroups
    ADD CONSTRAINT smf_membergroups_pkey PRIMARY KEY (id_group);


--
-- TOC entry 2755 (class 2606 OID 19511)
-- Dependencies: 251 251 2797
-- Name: smf_members_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_members
    ADD CONSTRAINT smf_members_pkey PRIMARY KEY (id_member);


--
-- TOC entry 2757 (class 2606 OID 19513)
-- Dependencies: 253 253 2797
-- Name: smf_message_icons_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_message_icons
    ADD CONSTRAINT smf_message_icons_pkey PRIMARY KEY (id_icon);


--
-- TOC entry 2759 (class 2606 OID 19515)
-- Dependencies: 255 255 2797
-- Name: smf_messages_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_messages
    ADD CONSTRAINT smf_messages_pkey PRIMARY KEY (id_msg);


--
-- TOC entry 2761 (class 2606 OID 19517)
-- Dependencies: 256 256 256 2797
-- Name: smf_moderators_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_moderators
    ADD CONSTRAINT smf_moderators_pkey PRIMARY KEY (id_board, id_member);


--
-- TOC entry 2763 (class 2606 OID 19519)
-- Dependencies: 257 257 257 2797
-- Name: smf_openid_assoc_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_openid_assoc
    ADD CONSTRAINT smf_openid_assoc_pkey PRIMARY KEY (server_url, handle);


--
-- TOC entry 2765 (class 2606 OID 19521)
-- Dependencies: 259 259 2797
-- Name: smf_package_servers_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_package_servers
    ADD CONSTRAINT smf_package_servers_pkey PRIMARY KEY (id_server);


--
-- TOC entry 2767 (class 2606 OID 19523)
-- Dependencies: 261 261 2797
-- Name: smf_permission_profiles_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_permission_profiles
    ADD CONSTRAINT smf_permission_profiles_pkey PRIMARY KEY (id_profile);


--
-- TOC entry 2769 (class 2606 OID 19525)
-- Dependencies: 262 262 262 2797
-- Name: smf_permissions_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_permissions
    ADD CONSTRAINT smf_permissions_pkey PRIMARY KEY (id_group, permission);


--
-- TOC entry 2771 (class 2606 OID 19527)
-- Dependencies: 264 264 2797
-- Name: smf_personal_messages_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_personal_messages
    ADD CONSTRAINT smf_personal_messages_pkey PRIMARY KEY (id_pm);


--
-- TOC entry 2773 (class 2606 OID 19529)
-- Dependencies: 265 265 265 2797
-- Name: smf_pm_recipients_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_pm_recipients
    ADD CONSTRAINT smf_pm_recipients_pkey PRIMARY KEY (id_pm, id_member);


--
-- TOC entry 2775 (class 2606 OID 19531)
-- Dependencies: 267 267 2797
-- Name: smf_pm_rules_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_pm_rules
    ADD CONSTRAINT smf_pm_rules_pkey PRIMARY KEY (id_rule);


--
-- TOC entry 2777 (class 2606 OID 19533)
-- Dependencies: 268 268 268 2797
-- Name: smf_poll_choices_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_poll_choices
    ADD CONSTRAINT smf_poll_choices_pkey PRIMARY KEY (id_poll, id_choice);


--
-- TOC entry 2779 (class 2606 OID 19535)
-- Dependencies: 270 270 2797
-- Name: smf_polls_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_polls
    ADD CONSTRAINT smf_polls_pkey PRIMARY KEY (id_poll);


--
-- TOC entry 2781 (class 2606 OID 19537)
-- Dependencies: 272 272 2797
-- Name: smf_scheduled_tasks_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_scheduled_tasks
    ADD CONSTRAINT smf_scheduled_tasks_pkey PRIMARY KEY (id_task);


--
-- TOC entry 2783 (class 2606 OID 19539)
-- Dependencies: 273 273 2797
-- Name: smf_sessions_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_sessions
    ADD CONSTRAINT smf_sessions_pkey PRIMARY KEY (session_id);


--
-- TOC entry 2785 (class 2606 OID 19541)
-- Dependencies: 274 274 2797
-- Name: smf_settings_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_settings
    ADD CONSTRAINT smf_settings_pkey PRIMARY KEY (variable);


--
-- TOC entry 2787 (class 2606 OID 19543)
-- Dependencies: 276 276 2797
-- Name: smf_smileys_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_smileys
    ADD CONSTRAINT smf_smileys_pkey PRIMARY KEY (id_smiley);


--
-- TOC entry 2789 (class 2606 OID 19545)
-- Dependencies: 278 278 2797
-- Name: smf_spiders_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_spiders
    ADD CONSTRAINT smf_spiders_pkey PRIMARY KEY (id_spider);


--
-- TOC entry 2791 (class 2606 OID 19547)
-- Dependencies: 280 280 2797
-- Name: smf_subscriptions_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_subscriptions
    ADD CONSTRAINT smf_subscriptions_pkey PRIMARY KEY (id_subscribe);


--
-- TOC entry 2793 (class 2606 OID 19549)
-- Dependencies: 281 281 281 281 2797
-- Name: smf_themes_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_themes
    ADD CONSTRAINT smf_themes_pkey PRIMARY KEY (id_theme, id_member, variable);


--
-- TOC entry 2795 (class 2606 OID 19551)
-- Dependencies: 283 283 2797
-- Name: smf_topics_pkey; Type: CONSTRAINT; Schema: forum; Owner: -
--

ALTER TABLE ONLY smf_topics
    ADD CONSTRAINT smf_topics_pkey PRIMARY KEY (id_topic);


-- Completed on 2013-12-08 21:05:11 CET

--
-- PostgreSQL database dump complete
--

SET search_path = public, pg_catalog;