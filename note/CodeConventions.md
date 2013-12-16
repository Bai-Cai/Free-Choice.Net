
1. General:

	platform and libraries standard:
		Jre			7
		Java EE 	6
		MyEclipse 	2013 //optional
		Spring 		3.1
		Struts		2
	

2. SQL code convention:

	table with 'R_' perfix connects two/multi entities

	table with 'FC_' (stands for 'free-choice') 
		indicate that this is an entity table


	primary key starts with '_', e.g. id
	foreign key ends with '_', e.g. id_user_

		all name 				-> varchar(30)
		password, email, tag 	-> varchar(40)
		other					-> text

		date 				 	-> java.sql.Date
		timestamp 			 	-> java.sql.TimeStamp


	date 				 -> java.sql.Date
	timestamp 			 -> java.sql.TimeStamp

	email				-> 	varchar(64)

	all name				-> varchar(64)

	abstract		varchar(256),
	title 			varchar(128),

	FC_Tag.content  varchar(64)

	FC_Post.password -> varchar(64)

	FC_Post.content 		-> java.sql.Clob

	gender:boolean. true for female, false for male.
	
	other				-> text


	status:		0 -> draft
	 			1 -> pendinf
				2 -> pulished
				3 -> freezed	# caonnot edit
				4 -> closed		# cannot comment



3. Java code convention:
		 

	Class names and Field names follow the usual convention(e.g. XxxYxxZzz),
		except for models(to consist with SQL)

	When using Hibernate, conventions of the class to a table are:
		all 'is_valid' has a default value: true(e.g. 'boolean is_valid = true;')
		and the annotation for the primary key is
		
			@Id
			@GeneratedValue(strategy = GenerationType.IDENTITY)
			private int _id;						//  ^ note here
	
	
	all entities have default methods inheritated from 'IDao'
	
	DAO name conventions:
			DAO interface is named as IDao_Xyz, e.g. 'IDao_User'
									  ^ interface
				and they are put into package 'net.freechoice.dao'
				
			DAO implementation is named as DAO_Xxx, 'DAO_User'
				and they are put into package 'net.freechoice.dao.impl'
				
	DAO 'get' method name convention:
		
		getXxx(Yyy z) with parameter 'z' such that 'z' is one field of Xxx, use 'By' clause
				getXxxByZzz(zzz)
			e.g.	
				List<FC_Tag>	getTagsByName(String name);
		
		getXxx(Yyy z) with parameter 'z' such that z is not any field of Xxx(likely a entity 
		 related to Xxx), use 'Of' clause
			e.g.
				List<FC_Post>	getPostsOfTag(int tagId);
		
		getXxx(Yyy z) with parameter 'z' such that 'z' is of time type, use 'on' clause
			e.g.
				List<FC_Transaction> getTransactionsOnDate(Date date);
	
		If the given parameter is not the unique field of this Object(i.e. not the id)
		returns a List, even if there is one and one only element in this list
			e.g.
				List<FC_User> 	getUsersByName(String name);// login name, unique
				
		when return type is a list, user suffix 's'
				getXxxsByYyy or getXxxsOfYyy
			e.g.				      ^
				List<FC_Post>	getPostsOfTag(int tagId);
									   ^
			Otherwise do not add 's' or 'es'.				
						
4. Other conventions:

	all identifier (sql views to java class,  sql column to java field)
		in SQL and Java code have exactly the same name, no exception!

	DO NOT change/edit database generated date/timestamp values in java program 
		unless you know exactly what you are doing!
		e.g., 

			time_posted		timestamp	NOT NULL DEFAULT current_timestamp,

			time_committed	timestamp	NOT NULL DEFAULT current_timestamp,

			date_created	date 		NOT NULL DEFAULT current_date,

		they are read-only, do not try to change them in you program.
	
	
	
	
	