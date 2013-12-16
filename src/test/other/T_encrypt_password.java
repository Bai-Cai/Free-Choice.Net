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
package test.other;

import java.security.MessageDigest;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.freechoice.service.EncryptionService;
import net.freechoice.util.Base64;

import org.apache.commons.lang.text.StrBuilder;
import org.junit.Test;

/**
 * @author BowenCai
 * 
 */

/**
 * result:
 * 
 * 
	    final int pow = 8;
	    final int loopNum = 100;
	    final int taskNum = 30;
	    
2820635592
2716452734
2851409771
2970407435
3033134729
3043758103
3086305592
3158056978
3127060579
3150998976
3122522871
3172129249
3141736596
3175485344
3253742297
3246114907
3324439690

3007340305
768000
arg: 3915

final int pow = 15;
final int loopNum = 30;
final int taskNum = 10;
49586348426
50239925265
50328132237
50150958365
50264798513
50396265599
50367384769
50880269625
50540419419
50563691599

 	total: 50331819382  9830400
 	arg:   5120


final int pow = 18;
final int loopNum = 50;
final int taskNum = 10;
637958991561
644509740286
644286290689
644428333399
645121364463
646674066780
647115938318
649427503409
648547078945
649846541418

645792000000

 total: 	6.45792 10^12 131072000
 arg:		4927

 *
 */
public class T_encrypt_password {

//	@Test
//	public static void test1() throws InterruptedException {
//		
//		MemoryMXBean memb = ManagementFactory.getMemoryMXBean();
//		OperatingSystemMXBean osb = 
//				ManagementFactory.newPlatformMXBeanProxy(mbsc, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
//		
//		
////		OperatingSystemMXBean osb =
////				(OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
//		memb.setVerbose(true);
//		osb.getSystemLoadAverage();
////		ManagementFactory.getPlatformMBeanServer().
////		ManagementFactory.getThreadMXBean().
////		osb.getSystemCpuLoad();
//		while(true) {
//			
//			System.out.println("Heap:" + memb.getHeapMemoryUsage());
//			System.out.println("getNonHea:" + memb.getNonHeapMemoryUsage());
////			System.out.println("CpuLoad: " + osb.getSystemCpuLoad());
//			System.out.print("LoadAverage:" + osb.getSystemLoadAverage());
////			ManagementFactory.getRuntimeMXBean().
//			int sessionCount;
////			ManagementFactory.getOperatingSystemMXBean().
//
//			System.out.println("\t" + 
//			( (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean() )
//			.getSystemLoadAverage());
//			Thread.sleep(5000);
//			System.out.println("-----------------------------------------------");
//		}
//	}

	@Test
	public void tt() {
		
		int i = 1<<24;
		int[][] ii = new int[1<<24][];
		String[] ss = new String[1<<24];
		while(--i != 0) {
			ii[i] = new int[1<<4];
			ss[i] = new String("dsfseofoc,wehxru,awoxeru,s");
		}
		System.err.println("xxxxxxxxx");
		System.out.println(ii[1<<10][6]);
	}
	@Test
	public void testxx() {

	    final Random random = new Random();
	    
	    final int pow = 15;
	    final int loopNum = 30;
	    final int taskNum = 10;

	    
		class MyTask implements Runnable {

			public void run() {

			    final Long[] elapsed = new Long[loopNum];
			    
				for (int i = 0; i != loopNum; ++i) {

					String[] safes = new String[1 << pow];
					elapsed[i] = 0L;

					for (int k = 0; k < 1 << pow; ++k) {
						safes[i] = getRandomString(random.nextInt(50));
//						assert (safes[i] != null);
					}

					long t1 = System.nanoTime();
					for (int j = 0; j < 1 << pow; j++) {
						String tmp = safes[i];
						// assert(safes[i] != null) : " index:" + j;
						safes[i] = EncryptionService.transformPassword(tmp);
					}
					long t2 = System.nanoTime();

					elapsed[i] += t2 - t1;
				}
				long total = 0;
				for (Long long1 : elapsed) {
					total += long1;
				}
				System.out.println(total); // 囧 return value by stdOut!
			}
		}

//		Thread thread = new Thread(new MyTask());
//		thread.run();
		ThreadPoolExecutor pool = 
				new ThreadPoolExecutor(2, 16, 10, TimeUnit.SECONDS,
						new SynchronousQueue<Runnable>(),
						new ThreadPoolExecutor.CallerRunsPolicy());
		
		for (int i = 0; i < taskNum; ++i) {
			pool.execute(new MyTask());
		}
		
	}
	
	public static final String pswTemp = "should declare foo2 as void foo2(<b>const</b> char* in2)? It all depends on whether foo2 actually modifies its argument or not. If it does, then there is no way you can declare in2 as <b>const</b>, consequently you cannot declare ... taking a <b>const</b> char* either. Then the type of foo should be void foo(char* in), not having the <b>const</b>.There is no way you should use <b>const</b> in foo then, because the helper foo2 modifies ... oth虽然现在图灵更加有名，然而在现实的程序设计中，却是丘奇的理论在起着绝大部分的作用。据我的经验，丘奇的理论让很多事情变得简单，而图灵的机器却过度的复杂。丘奇所发明的 lambda calculus 以及后续的工作，是几乎一切程序语言的理论基础。而根据老一辈的计算机工程师们的描述，最早的计算机构架也没有受到图灵的启发，那是一些电机工程师完全独立的工作。然而有趣的是，继承了丘奇衣钵的计算机科学家们拿到的那个大奖，仍然被叫做“图灵奖”。我粗略的算了一下，在迄今所有的图灵奖之中，程序语言的研究者占了近三分之一er people and they may not follow the same principles I’m describing 如果你查一下数学家谱图，就会发现丘奇其实是图灵的博士导师。然而从 Andrew Hodges 所著的《图灵传》，你却可以看到图灵的心目中仿佛并没有这个导师，仿佛自己的“全新发明”应得的名气，被丘奇抢走了一样（注意作者的用词：robbed）。事实到底是怎样的，恐怕谁也说不清楚。我只能说，貌似计算机科学从诞生之日开始就充满了各种“宗教斗争”。here. They may not have <b>const</b> on their parameter types even though they haven’t modified the parameter in their function想必世界上所有的计算机学生都知道图灵的大名和事迹，因为美国计算机器学会（ACM）每年都会颁发“图灵奖”，它被誉为计算机科学的最高荣誉。大部分的计算机学生都会在某门课程（比如“计算理论”）学习“图灵机”的原理。然而，有多少人知道丘奇是什么人，他做出了什么贡献，他与图灵是什么样的关系呢？我想恐怕不到一半的人吧。 body. This is not their fault";

	public static String getRandomString(int length) {

		StringBuilder sb = new StringBuilder(length);
	    Random random = new Random();
	    for (int i = 0; i < length; i++) {
	        char c = pswTemp.charAt(random.nextInt(pswTemp.length()));
	        sb.append(c);
	    }
	    return sb.toString();
	}
	

	
	
	
	//roller
    public static String encodePassword(String password, String algorithm) {
        byte[] unencodedPassword = password.getBytes();
        
        MessageDigest md = null;
        
        try {
            // first create an instance, given the provider
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
//            mLogger.error("Exception: " + e);l
            return password;
        }
        
        md.reset();
        
        // call the update method one or more times
        // (useful when you don't know the size of your data, eg. stream)
        md.update(unencodedPassword);
        
        // now calculate the hash
        byte[] encodedPassword = md.digest();
System.err.println("bytes:" + encodedPassword);
        StringBuffer buf = new StringBuffer();
        
        for (int i = 0; i < encodedPassword.length; i++) {
            if ((encodedPassword[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            
            buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
        }
System.out.println(Base64.encodeToString(encodedPassword).getBytes().length);
System.err.println("final : " + buf.toString().getBytes().length + "   " + buf.toString());
        return buf.toString();
    }
}


