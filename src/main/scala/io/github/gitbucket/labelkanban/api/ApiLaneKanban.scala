package io.github.gitbucket.labelkanban.api

import gitbucket.core.api.{ApiPath, FieldSerializable}
import gitbucket.core.model.{Label, Milestone, Priority}
import gitbucket.core.util.RepositoryName
import gitbucket.core.view.helpers

case class ApiLaneKanban(
                          id : String,
                          name : String,
                          color : String,
                          iconImage : String,
                          icon : String,
                          htmlUrl : Option[ApiPath],
                          switchUrl : Option[ApiPath],
                          paramKey: String,
                          order: Int
                        ) extends FieldSerializable {
}

object ApiLaneKanban {
  def apply(label: Label, repositoryName: RepositoryName, order: Int): ApiLaneKanban =
    ApiLaneKanban(
      id = label.labelId.toString,
      name = label.labelName,
      color = label.color,
      iconImage = "",
      icon = "",
      htmlUrl = Some(ApiPath(s"/${repositoryName.fullName}/issues?labels=${helpers.urlEncode(label.labelName)}&state=open")),
      switchUrl = Some(ApiPath(s"/api/v3/repos/${repositoryName.fullName}/plugin/labelkanban/label/${label.labelId}/switch/issue/")),
      paramKey = "label",
      order = order
    )

  def apply(milestone: Milestone, repositoryName: RepositoryName, order: Int): ApiLaneKanban =
    ApiLaneKanban(
      id = milestone.milestoneId.toString,
      name = milestone.title,
      color = "838383",
      iconImage = "",
      icon = "",
      htmlUrl = Some(ApiPath(s"/${repositoryName.fullName}/issues?milestone=${helpers.urlEncode(milestone.title)}&state=open")),
      switchUrl = Some(ApiPath(s"/api/v3/repos/${repositoryName.fullName}/plugin/labelkanban/milestone/${milestone.milestoneId}/switch/issue/")),
      paramKey = "milestone",
      order = order
    )

  def apply(userName: String, repositoryName: RepositoryName, order: Int): ApiLaneKanban =
    ApiLaneKanban(
      id = userName,
      name = userName,
      color = "838383",
      iconImage = "",
      icon = "",
      htmlUrl = Some(ApiPath(s"/${repositoryName.fullName}/issues?assigned=${helpers.urlEncode(userName)}&state=open")),
      switchUrl = Some(ApiPath(s"/api/v3/repos/${repositoryName.fullName}/plugin/labelkanban/assignee/${userName}/switch/issue/")),
      paramKey = "assignee",
      order = order
    )

  def apply(priority: Priority, repositoryName: RepositoryName, order: Int): ApiLaneKanban =
    ApiLaneKanban(
      id = priority.priorityId.toString(),
      name = priority.priorityName,
      color = priority.color,
      iconImage = "",
      icon = "",
      htmlUrl = Some(ApiPath(s"/${repositoryName.fullName}/issues?priority=${helpers.urlEncode(priority.priorityName)}&state=open")),
      switchUrl = Some(ApiPath(s"/api/v3/repos/${repositoryName.fullName}/plugin/labelkanban/priority/${priority.priorityId}/switch/issue/")),
      paramKey = "priority",
      order = order
    )
}


