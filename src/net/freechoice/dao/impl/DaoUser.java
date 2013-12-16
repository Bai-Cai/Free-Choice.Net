/*******************************************************************************
 * Copyright (c) 2013 BowenCai.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     BowenCai - initial API and implementation
 ******************************************************************************/
package net.freechoice.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import net.freechoice.dao.IDaoUser;
import net.freechoice.dao.annotation.Trigger;
import net.freechoice.misc.annotation.Warning;
import net.freechoice.model.FC_User;
import net.freechoice.model.orm.Map_User;
import net.freechoice.model.role.AvgUser;
import net.freechoice.model.role.SuperUser;
import net.freechoice.util.DateUtil;

/**
 * 
 * @author BowenCai
 *
 */
public class DaoUser extends DaoTemplate<FC_User> implements IDaoUser {

	public DaoUser() {
		super(FC_User.class, new Map_User());
	}


	private static final String SELECT_FROM_FC_USER = 
				"SELECT	id,"
				+"path_avatar,"
				+"name_login,"
				+"name_display,"
				+"email,"
				+"password,"
				+"tagline"
				+" from fc_user ";
	@Override
	public FC_User getById(int id) {

		return getJdbcTemplate().queryForObject(
				SELECT_FROM_FC_USER
				+"where is_valid = true and id = " + id, mapper);
	}
	
	/**
	 * @param user id
	 * @return if this user is still valid
	 */
	@Override
	public boolean isUserValid(int id) {

		return 1 == getJdbcTemplate().queryForInt(
			"select count(*) from fc_user where id = " + id);
	}
	/**
	 * @param user id
	 * @return if this user is still valid
	 */
	@Override
	public boolean isUserValid(final String name) {
		
		return 1 == getJdbcTemplate().query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				
				PreparedStatement ps = con.prepareStatement(
				"select count(1) from fc_user "
				+ "where is_valid = true and name_login = ?");
				ps.setString(1, name);
				return ps;
			}
		}, INT_EXTRACTOR);
	}
	
	/*
select u.*, m.value from fc_user as u left outer join fc_user_meta as m
on m.id_user_ = u.id 
where m.key = 'TIME_EXPIRE' and u.id = 8 
	 */
	@Warning(values = { "injection" })
	@Deprecated
	@Override
	public AvgUser	getRoleByEmail(final String email) {
		
		AvgUser role = null;

		SqlRowSet rowSet = getJdbcTemplate().queryForRowSet(
				"select U.id, U.password, U.name_login, M.value "
				+ " from FC_User as U left outer join FC_User_Meta as M "
						+" on U.id = M.id_user_ "
				+" where U.is_valid = true and email = " + quote(email)
				+" and M.key = " + SuperUser.TIME_EXPIRE
				);
		if (rowSet.next()) {
			if (rowSet.isLast()) {
				/**
				 * only super user have expire time
				 */
				String timeExpire = rowSet.getString(4);
				if (timeExpire == null 
						||( timeExpire!= null 
							&& DateUtil.hasExpired(timeExpire))) {
					
					role = new AvgUser();
				} else {
					role = new SuperUser();
				}
				role.id = rowSet.getInt(1);
				role.hashedPswWithSalt = rowSet.getString(2);
				role.name_login = rowSet.getString(3);
				role.email = email;
			} else {
				throw new RuntimeException(
						"multiple user found, should be one only");
			}
		}
		return role;
	}
	
	@Deprecated
	@Override
	public AvgUser	getRoleByLoginName(final String loginName) {
		
		AvgUser role = null;
		
		SqlRowSet rowSet = getJdbcTemplate().queryForRowSet(
				"select U.id, U.password, U.email, M.value "
				+ " from FC_User as U left outer join FC_User_Meta as M "
						+" on U.id = M.id_user_ "
				+" where U.is_valid = true and name_login = " + quote(loginName)
				+" and M.key = " + quote(SuperUser.TIME_EXPIRE)
				);

//System.err.println("SQL:" + 
//				"select U.id, U.password, U.email, M.value "
//				+ " from FC_User as U left outer join FC_User_Meta as M "
//						+" on U.id = M.id_user_ "
//				+" where U.is_valid = true and name_login = " + quote(loginName)
//				+" and M.key = " + quote(SuperUser.TIME_EXPIRE)
//		);

		if (rowSet.next()) {
			if (rowSet.isLast()) {
				/**
				 * only super user have expire time
				 */
				String timeExpire = rowSet.getString(4);
				if (timeExpire == null 
						||( timeExpire!= null 
							&& DateUtil.hasExpired(timeExpire))) {
					
					role = new AvgUser();
				} else {
					role = new SuperUser();
				}
				role.id = rowSet.getInt(1);
				role.hashedPswWithSalt = rowSet.getString(2);
				role.email = rowSet.getString(3);
				role.name_login = loginName;
			} else {
				throw new RuntimeException(
						"multiple user found, should be one only");
			}
		}
		return role;
	}

	@Deprecated
	@Override
	public Result getPasswordOfUser(String nameOrEmail) {
		
		SqlRowSet rowSet;
		if (nameOrEmail.contains("@")) {
			 rowSet = getJdbcTemplate().queryForRowSet(
						"select id, password from fc_user " 
						+ "where is_valid = true and email = " 
								+ quote(nameOrEmail));
		} else {
			 rowSet = getJdbcTemplate().queryForRowSet(
						"select id, password from fc_user " 
						+ "where is_valid = true and name_login = " 
								+ quote(nameOrEmail));
		}
		
		if (rowSet.next()) {
			if (rowSet.isLast()) {
				Result result = new Result();
				result.id = rowSet.getInt(1);
				result.password = rowSet.getString(2);
				return result;
			} else {
				throw new RuntimeException("multiple user found, should be one only");
			}
		} else {
			return null;
		}
	}
		
	/**
	 * @deprecated  login use getPasswordOfUser,
	 * 				 and then get user entity using getByid
	 * @param  login name or email of a user
	 *  	MUST be checked before using  
	 *	net.freechoice.helper.util.StringUtil.isloginNameLegal()
	 */
	@Deprecated()
	@Warning(values={"Injection"})
	@Override
	public FC_User getUsersByLoginString(final String nameLoginOrEmail) {

		if (nameLoginOrEmail.contains("@")) {
			
			return getJdbcTemplate().queryForObject(
					SELECT_FROM_FC_USER
					+"where is_valid = true and name_login = " 
								+ quote(nameLoginOrEmail),
					mapper);
		} else {
			
			return getJdbcTemplate().queryForObject(
					SELECT_FROM_FC_USER
					+"where is_valid = true and name_login = " + quote(nameLoginOrEmail),
					mapper);
		}
	}
	
	/** check whether this name has been registered
	 * @param  login name of a user, must be unique
	 *  	MUST be checked before using  
	 *	net.freechoice.helper.util.StringUtil.isloginNameLegal()
	 */
	@Warning(values={"Injection"})
	@Override
	public boolean isLoginNameUnique(final String name) {
		
		return 0 == getJdbcTemplate().query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(
					"select count(1) from fc_user "
					+" where is_valid = true and name_login = ?");
				ps.setString(1, name);
				return ps;
			}
		}, INT_EXTRACTOR);
		
	}
	
	/** check whether this email has been registered
	 * 	can be omitted because usually
	 *  user with unique login name will use unique email
	 * 
	 * @param  register email of a user
	 *  	MUST be checked before using  
	 *	net.freechoice.helper.util.StringUtil.isEmailLegal()
	 */
	@Warning(values={"Injection"})
	@Override
	public boolean isEmailUnique(final String email) {

		return 0 == getJdbcTemplate().query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(
				"select count(1) from fc_user "
				+"where is_valid = true and email = ?");
				ps.setString(1, email);
				return ps;
			}
		}, INT_EXTRACTOR);
	}

//	select U.*,
//	count(P.id) as p_count
//	 from v_user as U inner join v_post as P on U.id = P.id_author 
//	group by U.id, u.path_avatar, name_login, name_display, email, password, tagline
//	 order by p_count asc
//	 offset 0
//	 limit 10
	@Override
	public List<FC_User> getUsersByPostCount(int offset, int limit) {
		
		return getJdbcTemplate().query(
				"SELECT	id,"
				+"path_avatar,"
				+"name_login,"
				+"name_display,"
				+"email,"
				+"password,"
				+"tagline,"
				+"count(P.id) as p_count "
				+"from fc_user as U inner join fc_post as P on U.id = P.id_author "
				+"where U.is_valid = true and P.is_valid = true "
				+"group by U.id, U.path_avatar, "
				+		"U.name_login, U.name_display, "
				+		"U.email, U.password, U.tagline "
				+"order by p_count desc "
				+"offset " + offset
				+"limit " + limit
				, mapper);
	}
	
	@Override
	public FC_User getAuthorOfPost(int postId) {
		
		return getJdbcTemplate().queryForObject(
				SELECT_FROM_FC_USER +" as U inner join fc_post as P "
					+" on U.id = P.id_author "
				+" where U.is_valid = true" //  and P.is_valid = true 
				+" and P.id = " + postId, 
				mapper);
	}

	@Trigger
	@Override
	public void discard(int id) {
		getJdbcTemplate().execute(
			"begin transaction;"
			+"update fc_user set is_valid = false where id = " + id + ";"
			+ "update fc_profile set is_valid = false where id_user_ = " + id + ";"
			+ "update fc_account set is_valid = false where id_user_ = " + id + ";"
			+ "update FC_Transaction set is_valid = false " +
							"where id_account_ = " +
							"(select id from fc_account where id_user_ = " + id + ");"
			+ "update FC_Post "
			+"set name_author = " + NAME_INVALID_USER + " where id_author = " + id + ";"
			+"commit;"
		);
	}
	/*	
	 * UPDATE FC_Post SET id_author = -1,
						status = 4, --close
						name_author = 'user invalid'
		WHERE id_author = NEW.id;

	UPDATE FC_Project SET id_manager = -1,
							name_manager = 'user invalid'
		WHERE id_manager = OLD.id;

	UPDATE FC_Team set id_leader = -1,
						name_leader = 'user invalid'
		WHERE id_leader = OLD.id;
	 */
	
	@Trigger
	@Override
	public void delete(int id ) {
		getJdbcTemplate().execute(
			"begin transaction;"
			+"delete from fc_user where id = " + id + " ; "
			+ "update FC_Post "
			+ "set name_author = " + NAME_INVALID_USER + " where id_author = " + id + ';'
			+ "commit;"
		);
	}
	/*
			UPDATE FC_Profile SET is_valid = true 
			WHERE id_user_ = NEW.id;

			UPDATE FC_Account SET is_valid = true
			WHERE id_user_ = NEW.id;

			UPDATE R_team_user SET is_valid = true
			WHERE id_user_ = NEW.id;


			UPDATE FC_Project SET name_manager = (
								select name_display from FC_User where id = NEW.id_manager
							)
			WHERE id_manager = NEW.id;

			UPDATE FC_Team set name_leader = (
								select name_display from FC_User where id = NEW.id_leader
							)
			WHERE id_leader = NEW.id;

			UPDATE FC_Post SET name_author = (
								select name_display from FC_User where id = NEW.id_author
							),
							status = 1 -- pending
			WHERE id_author = NEW.id;
	 */
	@Trigger
	@Override
	public void recover(int id) {
		
		final String name = new StringBuilder("'")
				.append(
						
				getJdbcTemplate().queryForObject(
					"select name_display from fc_user "
					+"where id = " + id, 
					String.class)
						).append("'").toString();
		
		getJdbcTemplate().execute(
				 "begin transaction;"
				+"update fc_user set is_valid = true where id = " + id + ";"
				+ "update fc_profile set is_valid = true where id_user_ = " + id + ";"
				+ "update fc_account set is_valid = true where id_user_ = " + id + ";"
				+ "update FC_Post set name_author = " + name + " where id_author = " + id
				+ "update fc_Transaction set is_valid = true " +
								"where id_account_ = " +
								"(select id from fc_account where id_user_ = " + id + ");"
				+ "commit;"		
				);
	}
	
	
	@Trigger
	@Override
	public void	update(final FC_User user) {

		super.update(user);

		getJdbcTemplate().update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(
				"update FC_Post "
				+ " set name_author = ?"
				+ " where id_author = ?");
				ps.setString(1, user.name_display);
				ps.setInt(2, user.id);
				return ps;
			}
		});
	}
	
	@Override
	public String getValue(final int userId, final String key) {
		
		return getJdbcTemplate().query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(
				"select value from FC_User_Meta where id_user_ = ? "
				+ " and key = ?");
				ps.setInt(1, userId);
				ps.setString(2, key);
				return ps;
			}
		}, STR_EXTRACTOR);
	}
	
	@Override
	public void put(final int id_user_, final String key, final String value) {
		
		getJdbcTemplate().update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0)
					throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(
						"insert into fc_post_meta(id_user_, key, value)"
						+"values(?,?,?)"
						);
				ps.setInt(1, id_user_);
				ps.setString(2, key);
				ps.setString(3, value);
				return ps;
			}
		});
	}

}
