
name := "gitbucket-label-kanban-plugin"
organization := "io.github.gitbucket"
version := "3.7.1"
scalaVersion := "2.13.3"
gitbucketVersion := "4.37.2"

lazy val root = (project in file("."))
  .enablePlugins(SbtTwirl)
