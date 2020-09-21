package com.kero.security.spring.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.kero.security.core.agent.KeroAccessAgent;
import com.kero.security.core.agent.configurator.KeroAccessAgentConfigruatorBeans;
import com.kero.security.spring.config.KeroAccessAgentBean;
import com.kero.security.spring.config.KeroAccessAgentFactoryBean;
import com.kero.security.spring.config.KeroAccessAgentFactoryFtpConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {KeroAccessAgentBean.class, KeroAccessAgentFactoryFtpConfiguration.class, KeroAccessAgentConfigruatorBeans.class, KeroAccessAgentFactoryBean.class})
@ActiveProfiles("test")
public class MainTest {
	
	@Autowired
	private KeroAccessAgent agent;
	
	@Test
	public void test() {
		
		FakeFtpServer server = new FakeFtpServer();
			server.addUserAccount(new UserAccount("test", "test", "/home/test"));
			server.setServerControlPort(7777);
			
		FileSystem fileSystem = new UnixFakeFileSystem();
			fileSystem.add(new DirectoryEntry("/home/test"));
			fileSystem.add(new FileEntry("/home/test/scheme.k-s", "test"));
			fileSystem.add(new DirectoryEntry("/home/test/sub"));
			fileSystem.add(new FileEntry("/home/test/sub/scheme.k-s", "kek"));
			
		server.setFileSystem(fileSystem);
		
		server.start();

		agent.protect(Object.class, "OWNER");
		
		//Manual check by log
	}
}
