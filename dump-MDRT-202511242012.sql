--
-- PostgreSQL database cluster dump
--

-- Started on 2025-11-24 20:12:37

SET default_transaction_read_only = off;

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

--
-- Roles
--

CREATE ROLE "Zephyr";
ALTER ROLE "Zephyr" WITH SUPERUSER INHERIT CREATEROLE CREATEDB LOGIN REPLICATION BYPASSRLS;

--
-- User Configurations
--






--
-- Databases
--

--
-- Database "template1" dump
--

\connect template1

--
-- PostgreSQL database dump
--

-- Dumped from database version 14.8
-- Dumped by pg_dump version 17.0

-- Started on 2025-11-24 20:12:38

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: Zephyr
--

-- *not* creating schema, since initdb creates it


ALTER SCHEMA public OWNER TO "Zephyr";

--
-- TOC entry 3214 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: Zephyr
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2025-11-24 20:12:39

--
-- PostgreSQL database dump complete
--

--
-- Database "MDRT" dump
--

--
-- PostgreSQL database dump
--

-- Dumped from database version 14.8
-- Dumped by pg_dump version 17.0

-- Started on 2025-11-24 20:12:39

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3263 (class 1262 OID 31530)
-- Name: MDRT; Type: DATABASE; Schema: -; Owner: pg_database_owner
--

CREATE DATABASE "MDRT" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Japanese_Japan.1251';


ALTER DATABASE "MDRT" OWNER TO pg_database_owner;

\connect "MDRT"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 31531)
-- Name: docs; Type: SCHEMA; Schema: -; Owner: Zephyr
--

CREATE SCHEMA docs;


ALTER SCHEMA docs OWNER TO "Zephyr";

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: Zephyr
--

-- *not* creating schema, since initdb creates it


ALTER SCHEMA public OWNER TO "Zephyr";

--
-- TOC entry 228 (class 1255 OID 31779)
-- Name: recalculate_document_total(character varying); Type: FUNCTION; Schema: docs; Owner: Zephyr
--

CREATE FUNCTION docs.recalculate_document_total(doc_id character varying) RETURNS numeric
    LANGUAGE plpgsql
    AS $$
DECLARE
    total DECIMAL(15,2);
BEGIN
    SELECT COALESCE(SUM(item_amount), 0) INTO total
    FROM docs.detail 
    WHERE detail.doc_number = recalculate_document_total.doc_id;
    
    UPDATE docs.master
    SET total_amount = total 
    WHERE docs.master.doc_number = doc_id;
    
    RETURN total;
END;
$$;


ALTER FUNCTION docs.recalculate_document_total(doc_id character varying) OWNER TO "Zephyr";

--
-- TOC entry 229 (class 1255 OID 31780)
-- Name: update_document_total(); Type: FUNCTION; Schema: docs; Owner: Zephyr
--

CREATE FUNCTION docs.update_document_total() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        PERFORM docs.recalculate_document_total(NEW.doc_number);
        RETURN NEW;
    ELSIF TG_OP = 'UPDATE' THEN
        PERFORM docs.recalculate_document_total(NEW.doc_number);
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        PERFORM docs.recalculate_document_total(OLD.doc_number);
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$;


ALTER FUNCTION docs.update_document_total() OWNER TO "Zephyr";

--
-- TOC entry 227 (class 1255 OID 31778)
-- Name: update_updated_at_column(); Type: FUNCTION; Schema: docs; Owner: Zephyr
--

CREATE FUNCTION docs.update_updated_at_column() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$;


ALTER FUNCTION docs.update_updated_at_column() OWNER TO "Zephyr";

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 213 (class 1259 OID 31759)
-- Name: detail; Type: TABLE; Schema: docs; Owner: Zephyr
--

CREATE TABLE docs.detail (
    id integer NOT NULL,
    doc_number character varying(50) NOT NULL,
    item_name character varying(255) NOT NULL,
    item_amount numeric(15,2) DEFAULT 0,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE docs.detail OWNER TO "Zephyr";

--
-- TOC entry 3265 (class 0 OID 0)
-- Dependencies: 213
-- Name: TABLE detail; Type: COMMENT; Schema: docs; Owner: Zephyr
--

COMMENT ON TABLE docs.detail IS 'Список спецификаций документов';


--
-- TOC entry 3266 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN detail.doc_number; Type: COMMENT; Schema: docs; Owner: Zephyr
--

COMMENT ON COLUMN docs.detail.doc_number IS 'Номер документа';


--
-- TOC entry 3267 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN detail.item_name; Type: COMMENT; Schema: docs; Owner: Zephyr
--

COMMENT ON COLUMN docs.detail.item_name IS 'Имя спецификации';


--
-- TOC entry 3268 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN detail.item_amount; Type: COMMENT; Schema: docs; Owner: Zephyr
--

COMMENT ON COLUMN docs.detail.item_amount IS 'Сумма спецификации';


--
-- TOC entry 212 (class 1259 OID 31758)
-- Name: detail_id_seq; Type: SEQUENCE; Schema: docs; Owner: Zephyr
--

CREATE SEQUENCE docs.detail_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE docs.detail_id_seq OWNER TO "Zephyr";

--
-- TOC entry 3269 (class 0 OID 0)
-- Dependencies: 212
-- Name: detail_id_seq; Type: SEQUENCE OWNED BY; Schema: docs; Owner: Zephyr
--

ALTER SEQUENCE docs.detail_id_seq OWNED BY docs.detail.id;


--
-- TOC entry 211 (class 1259 OID 31745)
-- Name: master; Type: TABLE; Schema: docs; Owner: Zephyr
--

CREATE TABLE docs.master (
    id integer NOT NULL,
    doc_number character varying(50) NOT NULL,
    doc_date date DEFAULT now(),
    total_amount numeric(15,2) DEFAULT 0,
    comment text,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE docs.master OWNER TO "Zephyr";

--
-- TOC entry 3270 (class 0 OID 0)
-- Dependencies: 211
-- Name: TABLE master; Type: COMMENT; Schema: docs; Owner: Zephyr
--

COMMENT ON TABLE docs.master IS 'Список документов';


--
-- TOC entry 3271 (class 0 OID 0)
-- Dependencies: 211
-- Name: COLUMN master.doc_number; Type: COMMENT; Schema: docs; Owner: Zephyr
--

COMMENT ON COLUMN docs.master.doc_number IS 'Текстовый номер документа';


--
-- TOC entry 3272 (class 0 OID 0)
-- Dependencies: 211
-- Name: COLUMN master.doc_date; Type: COMMENT; Schema: docs; Owner: Zephyr
--

COMMENT ON COLUMN docs.master.doc_date IS 'Дата';


--
-- TOC entry 3273 (class 0 OID 0)
-- Dependencies: 211
-- Name: COLUMN master.total_amount; Type: COMMENT; Schema: docs; Owner: Zephyr
--

COMMENT ON COLUMN docs.master.total_amount IS 'а документа';


--
-- TOC entry 3274 (class 0 OID 0)
-- Dependencies: 211
-- Name: COLUMN master.comment; Type: COMMENT; Schema: docs; Owner: Zephyr
--

COMMENT ON COLUMN docs.master.comment IS 'Примечание';


--
-- TOC entry 210 (class 1259 OID 31744)
-- Name: master_id_seq; Type: SEQUENCE; Schema: docs; Owner: Zephyr
--

CREATE SEQUENCE docs.master_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE docs.master_id_seq OWNER TO "Zephyr";

--
-- TOC entry 3275 (class 0 OID 0)
-- Dependencies: 210
-- Name: master_id_seq; Type: SEQUENCE OWNED BY; Schema: docs; Owner: Zephyr
--

ALTER SEQUENCE docs.master_id_seq OWNED BY docs.master.id;


--
-- TOC entry 215 (class 1259 OID 31814)
-- Name: mdrt_log; Type: TABLE; Schema: docs; Owner: Zephyr
--

CREATE TABLE docs.mdrt_log (
    id integer NOT NULL,
    table_name character varying(50),
    success integer,
    message text,
    "timestamp" timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    record_data text DEFAULT '{}'::text
);


ALTER TABLE docs.mdrt_log OWNER TO "Zephyr";

--
-- TOC entry 3276 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN mdrt_log.success; Type: COMMENT; Schema: docs; Owner: Zephyr
--

COMMENT ON COLUMN docs.mdrt_log.success IS '1 - ошибка, 0 - успешно';


--
-- TOC entry 214 (class 1259 OID 31813)
-- Name: mdrt_log_id_seq; Type: SEQUENCE; Schema: docs; Owner: Zephyr
--

CREATE SEQUENCE docs.mdrt_log_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE docs.mdrt_log_id_seq OWNER TO "Zephyr";

--
-- TOC entry 3277 (class 0 OID 0)
-- Dependencies: 214
-- Name: mdrt_log_id_seq; Type: SEQUENCE OWNED BY; Schema: docs; Owner: Zephyr
--

ALTER SEQUENCE docs.mdrt_log_id_seq OWNED BY docs.mdrt_log.id;


--
-- TOC entry 3093 (class 2604 OID 31762)
-- Name: detail id; Type: DEFAULT; Schema: docs; Owner: Zephyr
--

ALTER TABLE ONLY docs.detail ALTER COLUMN id SET DEFAULT nextval('docs.detail_id_seq'::regclass);


--
-- TOC entry 3089 (class 2604 OID 31748)
-- Name: master id; Type: DEFAULT; Schema: docs; Owner: Zephyr
--

ALTER TABLE ONLY docs.master ALTER COLUMN id SET DEFAULT nextval('docs.master_id_seq'::regclass);


--
-- TOC entry 3096 (class 2604 OID 31817)
-- Name: mdrt_log id; Type: DEFAULT; Schema: docs; Owner: Zephyr
--

ALTER TABLE ONLY docs.mdrt_log ALTER COLUMN id SET DEFAULT nextval('docs.mdrt_log_id_seq'::regclass);


--
-- TOC entry 3106 (class 2606 OID 31765)
-- Name: detail detail_pkey; Type: CONSTRAINT; Schema: docs; Owner: Zephyr
--

ALTER TABLE ONLY docs.detail
    ADD CONSTRAINT detail_pkey PRIMARY KEY (id);


--
-- TOC entry 3102 (class 2606 OID 31757)
-- Name: master master_doc_number_key; Type: CONSTRAINT; Schema: docs; Owner: Zephyr
--

ALTER TABLE ONLY docs.master
    ADD CONSTRAINT master_doc_number_key UNIQUE (doc_number);


--
-- TOC entry 3104 (class 2606 OID 31755)
-- Name: master master_pkey; Type: CONSTRAINT; Schema: docs; Owner: Zephyr
--

ALTER TABLE ONLY docs.master
    ADD CONSTRAINT master_pkey PRIMARY KEY (id);


--
-- TOC entry 3112 (class 2606 OID 31823)
-- Name: mdrt_log mdrt_log_pkey; Type: CONSTRAINT; Schema: docs; Owner: Zephyr
--

ALTER TABLE ONLY docs.mdrt_log
    ADD CONSTRAINT mdrt_log_pkey PRIMARY KEY (id);


--
-- TOC entry 3110 (class 2606 OID 31767)
-- Name: detail unique_specification; Type: CONSTRAINT; Schema: docs; Owner: Zephyr
--

ALTER TABLE ONLY docs.detail
    ADD CONSTRAINT unique_specification UNIQUE (doc_number, item_name);


--
-- TOC entry 3107 (class 1259 OID 31776)
-- Name: idx_detail_document_id; Type: INDEX; Schema: docs; Owner: Zephyr
--

CREATE INDEX idx_detail_document_id ON docs.detail USING btree (doc_number);


--
-- TOC entry 3108 (class 1259 OID 31777)
-- Name: idx_detail_name; Type: INDEX; Schema: docs; Owner: Zephyr
--

CREATE INDEX idx_detail_name ON docs.detail USING btree (item_name);


--
-- TOC entry 3099 (class 1259 OID 31775)
-- Name: idx_master_date; Type: INDEX; Schema: docs; Owner: Zephyr
--

CREATE INDEX idx_master_date ON docs.master USING btree (doc_date);


--
-- TOC entry 3100 (class 1259 OID 31774)
-- Name: idx_master_number; Type: INDEX; Schema: docs; Owner: Zephyr
--

CREATE INDEX idx_master_number ON docs.master USING btree (doc_number);


--
-- TOC entry 3115 (class 2620 OID 31783)
-- Name: detail trigger_update_document_total_del; Type: TRIGGER; Schema: docs; Owner: Zephyr
--

CREATE TRIGGER trigger_update_document_total_del AFTER DELETE ON docs.detail FOR EACH ROW EXECUTE FUNCTION docs.update_document_total();


--
-- TOC entry 3116 (class 2620 OID 31781)
-- Name: detail trigger_update_document_total_ins; Type: TRIGGER; Schema: docs; Owner: Zephyr
--

CREATE TRIGGER trigger_update_document_total_ins AFTER INSERT ON docs.detail FOR EACH ROW EXECUTE FUNCTION docs.update_document_total();


--
-- TOC entry 3117 (class 2620 OID 31782)
-- Name: detail trigger_update_document_total_upd; Type: TRIGGER; Schema: docs; Owner: Zephyr
--

CREATE TRIGGER trigger_update_document_total_upd AFTER UPDATE ON docs.detail FOR EACH ROW EXECUTE FUNCTION docs.update_document_total();


--
-- TOC entry 3114 (class 2620 OID 31784)
-- Name: master update_documents_updated_at; Type: TRIGGER; Schema: docs; Owner: Zephyr
--

CREATE TRIGGER update_documents_updated_at BEFORE UPDATE ON docs.master FOR EACH ROW EXECUTE FUNCTION docs.update_updated_at_column();


--
-- TOC entry 3118 (class 2620 OID 31785)
-- Name: detail update_specifications_updated_at; Type: TRIGGER; Schema: docs; Owner: Zephyr
--

CREATE TRIGGER update_specifications_updated_at BEFORE UPDATE ON docs.detail FOR EACH ROW EXECUTE FUNCTION docs.update_updated_at_column();


--
-- TOC entry 3113 (class 2606 OID 31768)
-- Name: detail fk_specification_document; Type: FK CONSTRAINT; Schema: docs; Owner: Zephyr
--

ALTER TABLE ONLY docs.detail
    ADD CONSTRAINT fk_specification_document FOREIGN KEY (doc_number) REFERENCES docs.master(doc_number) ON DELETE CASCADE;


--
-- TOC entry 3264 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: Zephyr
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2025-11-24 20:12:39

--
-- PostgreSQL database dump complete
--

-- Completed on 2025-11-24 20:12:39

--
-- PostgreSQL database cluster dump complete
--

