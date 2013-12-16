package net.freechoice.application;

import java.util.Date;

import net.freechoice.application.load.Report;
import net.freechoice.dao.impl.DaoComment;
import net.freechoice.dao.impl.DaoPost;
import net.freechoice.util.WebContext;
import net.freechoice.util.StrUtil;

/**
 * @author BowenCai
 *
 */
public class SnapShooter {

	public static final String SNAPSHOT_SUBJ = "FC-Routin Report";
	
	SnapShooter() {
	}

	MailSender		mailSender;
	
	DaoPost		postDao;
	DaoComment 	commentDao;

	public String	template;
	
	public void setPostDao(DaoPost postDao) {
		this.postDao = postDao;
	}
	public void setCommentDao(DaoComment commentDao) {
		this.commentDao = commentDao;
	}
	public void setTemplate(String template) {
		this.template = template;
	}

	
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public static final String SNAPSHOT_NAME = "R2D2";
	public static final String SNAPSHOT_ADDRESS = "FC-SnapShot";
//	Time		:	%s
//	CPU load	:	%f
//	Heap Usage  :	%f
//	Non-Heap	:	%f
//	
//	Session		:	%d
//	Post		:	%d
//	Comment		:	%d
	public void handle(Report report) {
		
		mailSender.send(SNAPSHOT_NAME,// name
						SNAPSHOT_ADDRESS, // address
						SNAPSHOT_SUBJ, // subject
						StrUtil.format(template,
								WebContext.now().toString(),
								report.cpuLoad,
								report.heapUsage,
								report.nonHeapUsage,
								report.sessionCount,
								postDao.count(),
								commentDao.count()
								).toString()
						);
	}
	
	public static class SnapShot extends Report {

		Date time;
		
		SnapShot() {
			time = WebContext.now();
		}
		
		SnapShot(Report report) {
			super(report);
			time = WebContext.now();
		}
	}
	
}





