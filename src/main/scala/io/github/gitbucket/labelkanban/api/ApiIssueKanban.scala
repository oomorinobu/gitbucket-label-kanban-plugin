package io.github.gitbucket.labelkanban.api

import java.util.Date

import gitbucket.core.api._
import gitbucket.core.model.{Issue, Label, Priority}
import gitbucket.core.util.RepositoryName
import gitbucket.core.view.helpers

import scala.collection.mutable

case class ApiIssueKanban(
                           userName: String,
                           issueId: Int,
                           openedUserName: String,
                           milestoneId: Int,
                           priorityId: Int,
                           assignedUserName: String,
                           title: String,
                           content: String,
                           closed: Boolean,
                           registeredDate: java.util.Date,
                           updatedDate: java.util.Date,
                           isPullRequest: Boolean,
                           labelNames:List[String],
                           metrics: Map[String,String],
                           show: Boolean = false,
                         )(repositoryName: RepositoryName)
  extends FieldSerializable {
  val htmlUrl = ApiPath(s"/${repositoryName.fullName}/issues/${issueId}")
  val comments_url = ApiPath(s"/api/v3/repos/${repositoryName.fullName}/issues/${issueId}/comments")
}


object ApiIssueKanban {
  def apply(issue: Issue, labels: List[Label], prefix: String, repositoryName: RepositoryName): ApiIssueKanban =
    ApiIssueKanban(
      userName = issue.userName,
      issueId = issue.issueId,
      openedUserName = issue.openedUserName,
      milestoneId = issue.milestoneId.getOrElse(-1),
      priorityId = issue.priorityId.getOrElse(-1),
      assignedUserName = issue.assignedUserName.getOrElse(""),
      title = issue.title,
      content = issue.content.getOrElse(""),
      closed = issue.closed,
      registeredDate = issue.registeredDate,
      updatedDate = issue.updatedDate,
      isPullRequest = issue.isPullRequest,
      labelNames = labels.map(_.labelName),
      metrics = createMetrics(
        milestoneId = issue.milestoneId,
        priorityId = issue.priorityId,
        assignedUserName = issue.assignedUserName,
        labels = labels,
        prefix
      )
    )(repositoryName)

  def applySummary(issue: Issue, labels: List[Label], prefix: String, priorities: List[Priority]): ApiIssueKanban =
    ApiIssueKanban(
      userName = issue.userName,
      issueId = issue.issueId,
      openedUserName = issue.openedUserName,
      milestoneId = issue.milestoneId.getOrElse(-1),
      priorityId = issue.priorityId.getOrElse(-1),
      assignedUserName = issue.assignedUserName.getOrElse(""),
      title = issue.title,
      content = issue.content.getOrElse(""),
      closed = issue.closed,
      registeredDate = issue.registeredDate,
      updatedDate = issue.updatedDate,
      isPullRequest = issue.isPullRequest,
      labelNames = labels.map(_.labelName),
      metrics = createSummaryMetrics(
        labels = labels,
        prefix,
        priority = priorities.find(p => p.priorityId == issue.priorityId.getOrElse(-1)),
        repository = issue.repositoryName
      )
    )(RepositoryName(issue.userName, issue.repositoryName))

  def createMetrics(milestoneId: Option[Int],
                    priorityId: Option[Int],
                    assignedUserName: Option[String],
                    labels: List[Label],
                    prefix: String
                   ): Map[String, String] = Map(
    "None" -> "0",
    "Label:" + prefix -> labels.find(_.labelName.startsWith(prefix)).map(_.labelId).getOrElse(0).toString,
    "Milestones" -> milestoneId.getOrElse(0).toString,
    "Priorities" -> priorityId.getOrElse(0).toString,
    "Assignees" -> (assignedUserName match {
      case Some(s) if s.length > 0 => s
      case _ => "-"
    })
  )

  def createSummaryMetrics(
                            labels: List[Label],
                            prefix: String,
                            priority: Option[Priority],
                            repository: String
                          ): Map[String, String] = Map(
    "None" -> "",
    "Label:" + prefix -> labels.find(_.labelName.startsWith(prefix)).map(_.labelName).getOrElse(""),
    "Priorities" -> priority.map(_.priorityName).getOrElse(""),
    "Repositories" -> repository
  )
}