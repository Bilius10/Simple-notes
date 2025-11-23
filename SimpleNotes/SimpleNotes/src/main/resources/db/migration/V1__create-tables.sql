-- public."user" definição

-- Drop table

-- DROP TABLE public."user";

CREATE TABLE public."user" (
                               id serial4 NOT NULL,
                               "name" varchar(100) NOT NULL,
                               email varchar(100) NOT NULL,
                               "password" varchar(255) NOT NULL,
                               created_by varchar(100) NULL,
                               created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
                               updated_by varchar(100) NULL,
                               updated_at timestamp NULL,
                               "role" varchar NULL,
                               is_account_non_locked bool NULL,
                               CONSTRAINT user_email_key UNIQUE (email),
                               CONSTRAINT user_pkey PRIMARY KEY (id)
);


-- public.wallet definição

-- Drop table

-- DROP TABLE public.wallet;

CREATE TABLE public.wallet (
                               id serial4 NOT NULL,
                               "name" varchar(100) NOT NULL,
                               description varchar(100) NULL,
                               created_by varchar(100) NULL,
                               created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
                               updated_by varchar(100) NULL,
                               updated_at timestamp NULL,
                               CONSTRAINT wallet_pkey PRIMARY KEY (id)
);


-- public.friend_request definição

-- Drop table

-- DROP TABLE public.friend_request;

CREATE TABLE public.friend_request (
                                       id bigserial NOT NULL,
                                       sender_id int8 NOT NULL,
                                       receiver_id int8 NOT NULL,
                                       status varchar(20) NOT NULL,
                                       created_at timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                       CONSTRAINT friend_request_pkey PRIMARY KEY (id),
                                       CONSTRAINT fk_receiver FOREIGN KEY (receiver_id) REFERENCES public."user"(id) ON DELETE CASCADE,
                                       CONSTRAINT fk_sender FOREIGN KEY (sender_id) REFERENCES public."user"(id) ON DELETE CASCADE
);


-- public.note definição

-- Drop table

-- DROP TABLE public.note;

CREATE TABLE public.note (
                             id serial4 NOT NULL,
                             title varchar(50) NULL,
                             "content" text NULL,
                             wallet_id int4 NULL,
                             created_by varchar(100) NULL,
                             created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
                             updated_by varchar(100) NULL,
                             updated_at timestamp NULL,
                             CONSTRAINT note_pkey PRIMARY KEY (id),
                             CONSTRAINT note_wallet_id_fkey FOREIGN KEY (wallet_id) REFERENCES public.wallet(id) ON DELETE CASCADE
);


-- public.notifications definição

-- Drop table

-- DROP TABLE public.notifications;

CREATE TABLE public.notifications (
                                      id bigserial NOT NULL,
                                      user_id int8 NOT NULL,
                                      is_read bool DEFAULT false NOT NULL,
                                      created_at timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                      message varchar NOT NULL,
                                      CONSTRAINT notifications_pkey PRIMARY KEY (id),
                                      CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES public."user"(id) ON DELETE CASCADE
);


-- public.user_tokens definição

-- Drop table

-- DROP TABLE public.user_tokens;

CREATE TABLE public.user_tokens (
                                    id serial4 NOT NULL,
                                    user_id int4 NOT NULL,
                                    token_hash varchar(255) NOT NULL,
                                    token_type varchar(50) NOT NULL,
                                    is_used bool DEFAULT false NULL,
                                    created_at timestamptz DEFAULT CURRENT_TIMESTAMP NULL,
                                    CONSTRAINT user_tokens_pkey PRIMARY KEY (id),
                                    CONSTRAINT user_tokens_token_hash_key UNIQUE (token_hash),
                                    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES public."user"(id) ON DELETE CASCADE
);
CREATE INDEX idx_user_tokens_token_hash ON public.user_tokens USING btree (token_hash);


-- public.wallet_user definição

-- Drop table

-- DROP TABLE public.wallet_user;

CREATE TABLE public.wallet_user (
                                    wallet_id int4 NOT NULL,
                                    user_id int4 NOT NULL,
                                    created_by varchar(100) NULL,
                                    created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
                                    updated_by varchar(100) NULL,
                                    updated_at timestamp NULL,
                                    can_create bool DEFAULT false NULL,
                                    can_update bool DEFAULT false NULL,
                                    can_delete bool DEFAULT false NULL,
                                    can_view bool DEFAULT true NULL,
                                    id serial4 NOT NULL,
                                    CONSTRAINT wallet_user_pk PRIMARY KEY (id),
                                    CONSTRAINT wallet_user_user_id_fkey FOREIGN KEY (user_id) REFERENCES public."user"(id) ON DELETE CASCADE,
                                    CONSTRAINT wallet_user_wallet_id_fkey FOREIGN KEY (wallet_id) REFERENCES public.wallet(id) ON DELETE CASCADE
);