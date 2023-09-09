-- Table: public.produtos

-- DROP TABLE IF EXISTS public.produtos;

CREATE TABLE IF NOT EXISTS public.produtos
(
    id integer NOT NULL DEFAULT nextval('produtos_id_seq'::regclass),
    hash uuid DEFAULT gen_random_uuid(),
    nome character varying(255) COLLATE pg_catalog."default" NOT NULL,
    descricao text COLLATE pg_catalog."default" NOT NULL,
    ean13 character varying(13) COLLATE pg_catalog."default" NOT NULL,
    preco numeric(13,2) NOT NULL DEFAULT 0,
    quantidade numeric(13,2) NOT NULL DEFAULT 0,
    estoque_min numeric(13,2) NOT NULL DEFAULT 0,
    dtcreate timestamp without time zone,
    dtupdate timestamp without time zone,
    l_ativo boolean NOT NULL,
    CONSTRAINT produtos_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.produtos
    OWNER to postgres;
