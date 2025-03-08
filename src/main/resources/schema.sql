CREATE TABLE IF NOT EXISTS public.channels(
    id uuid NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    name character varying(100) COLLATE pg_catalog."default",
    description character varying(500) COLLATE pg_catalog."default",
    type character varying(10) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT channels_pkey PRIMARY KEY (id),
    CONSTRAINT channels_type_check CHECK (type::text = ANY (ARRAY['PUBLIC'::character varying, 'PRIVATE'::character varying]::text[]))
)