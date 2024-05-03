-- DROP SCHEMA anime;
CREATE SCHEMA anime AUTHORIZATION roo;

-- DROP TABLE anime.anime;

CREATE TABLE anime.anime (
	id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	"name" varchar NOT NULL,
	CONSTRAINT anime_pk PRIMARY KEY (id)
);

INSERT INTO anime.anime ("name") VALUES
	 ('Hellsing'),
	 ('dragon ball'),
	 ('one piece');
