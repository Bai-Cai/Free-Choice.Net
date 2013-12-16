



			-- PostgreSQL




-- table with 'R_' perfix connects two/multi entities

-- table with 'FC_' (stands for 'free-choice') 
-- 		indicate that this is an entity table

-- date 				 -> java.sql.Date
-- timestamp 			 -> java.sql.TimeStamp

-- email				-> 	char(48)

-- all name				-> char(32)

	--	title 			varchar(96),

	-- FC_Tag.content  varchar(32)

-- FC_Post
-- password -> varchar(48)

	-- comment 		varchar(2048)


-- gender:boolean. true for female, false for male.

-- status:	0 -> draft
-- 			1 -> pending
-- 			2 -> pulished
-- 			3 -> freezed	# caonnot edit
--			4 -> closed		# cannot comment

-- foreign key ends with '_', e.g. id_user_



DROP TABLE IF EXISTS 	FC_Post			CASCADE;
DROP TABLE IF EXISTS	FC_Post_Meta 	CASCADE;
DROP TABLE IF EXISTS 	FC_Comment 		CASCADE;
DROP TABLE IF EXISTS 	FC_Tag			CASCADE;
DROP TABLE IF EXISTS	R_tag_post 		CASCADE;

DROP TABLE IF EXISTS	FC_User			CASCADE;
DROP TABLE IF EXISTS	FC_User_Meta 	CASCADE;

DROP TABLE IF EXISTS	FC_Research_log CASCADE;

------------------------  User --------------------------------------
CREATE TABLE FC_User
(
	id				serial		PRIMARY KEY,
	is_valid 		boolean		NOT NULL DEFAULT true,

	path_avatar		varchar(32)	NOT NULL DEFAULT 'DEFAULT/avatar.png', -- !!!!!!!!!!!!!!!!!!!!!!
	-- path_photo		char(32)	NOT NULL DEFAULT 'avator_default.png',
	name_login		varchar(32) 	NOT NULL, -- unique value, checked in application
	name_display	varchar(32),
	email			varchar(48) 	NULL NULL,-- unique value, checked in application layer
	password		varchar(48) 	NOT NULL,

	tagline			varchar(96)
);

------------------------  Post --------------------------------------
CREATE TABLE FC_User_Meta
(
	id 				serial	NOT NULL PRIMARY KEY,
	id_user_		integer NOT NULL REFERENCES FC_User(id)
								ON DELETE CASCADE,

	key 			varchar(64),
	value			text
);
CREATE TABLE FC_Post
(
	id				serial		PRIMARY KEY,

	is_valid		boolean 	NOT NULL DEFAULT true,

	status			smallint	NOT NULL DEFAULT 0,
								-- CHECK (status >= 0
									   -- AND status < 5),

	id_author		integer		NOT NULL,-- if author is deleted,

	name_author		varchar(32)	NOT NULL, -- ref FC_User.name_display,
	-- to the dummmy if FC_User deleted or invalidated
	
	-- month_posted	smallint	NOT NULL 
	-- 							DEFAULT date_part('month', current_date),

	time_posted		timestamp	NOT NULL DEFAULT current_timestamp,

	num_read		integer		NOT NULL DEFAULT 0,
								-- CHECK (num_read >= 0),

	num_comment		integer		NOT NULL DEFAULT 0,
								-- CHECK (num_comment >= 0),

	title 			varchar(96)	NOT NULL,
	content			text		NOT NULL,
	-- search_vector	tsvector	NOT NULL
	search_vector	tsvector
);

CREATE TABLE FC_Post_Meta
(
	id 				serial	NOT NULL PRIMARY KEY,
	id_post_		integer NOT NULL REFERENCES FC_Post(id)
								ON DELETE CASCADE,

	key 			varchar(64),
	value			text
);

CREATE TABLE FC_Tag
(
	id				serial		PRIMARY KEY,
	is_valid		boolean		NOT NULL DEFAULT true,

	num_tagged		integer		NOT NULL DEFAULT 0,
								-- CHECK (num_tagged >= 0),

	content			varchar(32)	NOT NULL
);

CREATE TABLE FC_Comment
(
	id				serial		PRIMARY KEY,
	is_valid		boolean		NOT NULL DEFAULT true,

	id_post_		integer		NOT NULL REFERENCES FC_Post(id)
								ON DELETE CASCADE,

	time_posted		timestamp	NOT NULL DEFAULT current_timestamp,
	email 			varchar(48) 	NOT NULL,
	name 			varchar(32) 	NOT NULL,
	comment 		varchar(2048) NOT NULL
);


CREATE TABLE R_tag_post
(
	id_tag_			integer		NOT NULL REFERENCES FC_Tag(id)
								ON DELETE CASCADE,

	id_post_		integer		NOT NULL REFERENCES FC_Post(id)
								ON DELETE CASCADE,
								
	-- is_valid		boolean		NOT NULL DEFAULT true,
	PRIMARY KEY(id_post_, id_tag_)
);




------------------------  Research --------------------------------------

CREATE TABLE FC_Research
(

	id				serial		PRIMARY KEY,
	is_valid 		boolean		NOT NULL DEFAULT true,

	title			varchar(96) NOT NULL
);


CREATE TABLE FC_Research_log
(
	id				serial		PRIMARY KEY,
	is_valid 		boolean		NOT NULL DEFAULT true,

	id_research_		integer		NOT NULL REFERENCES FC_Research(id)
								ON DELETE CASCADE,

	name_author		varchar(32)	NOT NULL,

	time_posted		timestamp	NOT NULL DEFAULT current_timestamp,

	num_read		integer		NOT NULL DEFAULT 0,
								-- CHECK (num_read >= 0),

	title 			varchar(96)	NOT NULL,

	content			text		NOT NULL,
	-- search_vector	tsvector	NOT NULL
	search_vector	tsvector
);