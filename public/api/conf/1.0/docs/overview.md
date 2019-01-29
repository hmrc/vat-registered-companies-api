This API enables your application to “pull” business event notifications CDS has generated from requests submitted via the CDS APIs.

For example, if you submit a form to a CDS API the request is accepted with a HTTP status code 202 but then waits for further human intervention. After this intervention happens, CDS has a new notification for your application.

If  a URL to a client callback service has been provided in  subscription details, CDS will try to push this notification to your application,  where the push fails then this notification is sent to the notification pull queue. Failure can happen if your system is down, or your firewall rules prevent it, or you choose to use this pull method instead.

Where a notification has been sent to the pull queue, your application can now pull the notification from the queue using the Pull Notifications API.  Pull notifications remain queued for 14 days, after which notifications automatically delete from the queue.


The Pull Notifications API works in two discrete modes.  It is recommended that your application consumes the Pull Notifications API in one of the following modes:


## Retrieve and delete pull notifications

This mode is actioned via the two following endpoints:

GET /notifications - returns a list of all notifications id’s available to be pulled for a given Client id.

DELETE /notifications/{Id} - retrieve and delete the requested notification.


## Retrieve and persist pull notifications

This mode will persist a retrieved notification for a  maximum of 14 days from when the notification was generated, during this time the notification is persisted in the pull notification queue and can be retrieved any number of times.

The retrieve and persist mode is actioned via the two following endpoints:

GET /notifications/unpulled - returns a list of new notifications id’s only. With ‘new’ being defined as notifications that have not been previously pulled.

GET /notifications/unpulled/{notificationId} - returns an individual new notification. Instead of the previous DELETE operation,  pulled notifications are now retained with a ‘pulled’ status for a period of 14 days from the date the notification was generated.

GET /notifications/pulled - returns a list of previously pulled notification id's only, the response will contain id’s for all pulled notifications for a given Client id, for the past 14 days.

GET /notifications/pulled/{notificationId} - returns an individual, previously pulled notification up to 14 days from the date the notification was generated.

