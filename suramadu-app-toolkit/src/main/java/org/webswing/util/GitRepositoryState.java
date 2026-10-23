package org.webswing.util;

import java.util.Properties;

public class GitRepositoryState {

  private static GitRepositoryState instance;

  String tags; // =${git.tags} // comma separated tag names
  String branch; // =${git.branch}
  String dirty; // =${git.dirty}
  String remoteOriginUrl; // =${git.remote.origin.url}

  String commitId; // =${git.commit.id.full} OR ${git.commit.id}
  String commitIdAbbrev; // =${git.commit.id.abbrev}
  String describe; // =${git.commit.id.describe}
  String describeShort; // =${git.commit.id.describe-short}
  String commitUserName; // =${git.commit.user.name}
  String commitUserEmail; // =${git.commit.user.email}
  String commitMessageFull; // =${git.commit.message.full}
  String commitMessageShort; // =${git.commit.message.short}
  String commitTime; // =${git.commit.time}
  String closestTagName; // =${git.closest.tag.name}
  String closestTagCommitCount; // =${git.closest.tag.commit.count}

  String buildUserName; // =${git.build.user.name}
  String buildUserEmail; // =${git.build.user.email}
  String buildTime; // =${git.build.time}
  String buildHost; // =${git.build.host}
  String buildVersion; // =${git.build.version}

  public GitRepositoryState(Properties properties) {
    // Properties.getProperty returns null for missing keys. The previous
    // String.valueOf(properties.get(...)) turned every missing key into the literal
    // string "null", which then survived null checks downstream (e.g. the server
    // startup banner printing "version=null").
    this.tags = properties.getProperty("git.tags");
    this.branch = properties.getProperty("git.branch");
    this.dirty = properties.getProperty("git.dirty");
    this.remoteOriginUrl = properties.getProperty("git.remote.origin.url");

    this.commitId = properties.getProperty("git.commit.id.full"); // OR "git.commit.id"
    // depending on configuration
    this.commitIdAbbrev = properties.getProperty("git.commit.id.abbrev");
    this.describe = properties.getProperty("git.commit.id.describe");
    this.describeShort = properties.getProperty("git.commit.id.describe-short");
    this.commitUserName = properties.getProperty("git.commit.user.name");
    this.commitUserEmail = properties.getProperty("git.commit.user.email");
    this.commitMessageFull = properties.getProperty("git.commit.message.full");
    this.commitMessageShort = properties.getProperty("git.commit.message.short");
    this.commitTime = properties.getProperty("git.commit.time");
    this.closestTagName = properties.getProperty("git.closest.tag.name");
    this.closestTagCommitCount = properties.getProperty("git.closest.tag.commit.count");

    this.buildUserName = properties.getProperty("git.build.user.name");
    this.buildUserEmail = properties.getProperty("git.build.user.email");
    this.buildTime = properties.getProperty("git.build.time");
    this.buildHost = properties.getProperty("git.build.host");
    this.buildVersion = properties.getProperty("git.build.version");
  }


  public static GitRepositoryState getInstance() {
    if (instance == null) {
      Properties properties = new Properties();
      try {
        ClassLoader cl = GitRepositoryState.class.getClassLoader();
        if (cl == null) {
          cl = ClassLoader.getSystemClassLoader();
        }
        java.io.InputStream in = cl.getResourceAsStream("git.properties");
        if (in != null) {
          try (java.io.InputStream autoClose = in) {
            properties.load(autoClose);
          }
        } else {
          AppLogger.warn("git.properties not found on classpath — git build info unavailable");
        }
      } catch (Exception e) {
        AppLogger.warn("Failed to load git.properties");
      }
      instance = new GitRepositoryState(properties);
    }
    return instance;
  }

  public String getTags() {
    return tags;
  }

  public String getBranch() {
    return branch;
  }

  public String getDirty() {
    return dirty;
  }

  public String getRemoteOriginUrl() {
    return remoteOriginUrl;
  }

  public String getCommitId() {
    return commitId;
  }

  public String getCommitIdAbbrev() {
    return commitIdAbbrev;
  }

  public String getDescribe() {
    return describe;
  }

  public String getDescribeShort() {
    return describeShort;
  }

  public String getCommitUserName() {
    return commitUserName;
  }

  public String getCommitUserEmail() {
    return commitUserEmail;
  }

  public String getCommitMessageFull() {
    return commitMessageFull;
  }

  public String getCommitMessageShort() {
    return commitMessageShort;
  }

  public String getCommitTime() {
    return commitTime;
  }

  public String getClosestTagName() {
    return closestTagName;
  }

  public String getClosestTagCommitCount() {
    return closestTagCommitCount;
  }

  public String getBuildUserName() {
    return buildUserName;
  }

  public String getBuildUserEmail() {
    return buildUserEmail;
  }

  public String getBuildTime() {
    return buildTime;
  }

  public String getBuildHost() {
    return buildHost;
  }

  public String getBuildVersion() {
    return buildVersion;
  }

}
