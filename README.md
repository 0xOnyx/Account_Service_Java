# Account_Service_Java
Account Service (Java)
Companies send out payrolls to employees using corporate mail. This solution has certain disadvantages related to security and usability. In this project, put on a robe of such an employee. As you're familiar with Java and Spring Framework, you've suggested an idea of sending payrolls to the employee's account on the corporate website. The management has approved your idea, but it will be you who will implement this project. You've decided to start by developing the API structure, then define the role model, implement the business logic, and, of course, ensure the security of the service.

Work on project. Stage 5/7: The authorization

Description
Our service is almost ready; only the roles remain. We need to add the authorization. Authorization is a process when the system decides whether an authenticated client has permission to access the requested resource. Authorization always follows authentication.

Our service should implement the role model that we have developed earlier:

Anonymous	User	Accountant	Administrator
POST api/auth/signup	+	+	+	+
POST api/auth/changepass		+	+	+
GET api/empl/payment	-	+	+	-
POST api/acct/payments	-	-	+	-
PUT api/acct/payments	-	-	+	-
GET api/admin/user	-	-	-	+
DELETE api/admin/user	-	-	-	+
PUT api/admin/user/role	-	-	-	+
Tip: You can use the article Spring Security Roles and Permissions by JavadevJournal and Introduction to Spring Method Security by Baeldung to learn more about the authorization in Spring Boot.

We also advise you to take a look at the following articles; they may help you a lot: Spring Security – Customize the 403 Forbidden/Access Denied Page and Hibernate could not initialize proxy – no Session by Baeldung

Tip: Some hints on how to create a table with user roles at the start of the service:

@Component
public class DataLoader {

    private GroupRepository groupRepository;

    @Autowired
    public DataLoader(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
        createRoles();
    }

    private void createRoles() {
        try {
            groupRepository.save(new Group("ROLE_ADMINISTRATOR"));
            groupRepository.save(new Group("ROLE_USER"));
            groupRepository.save(new Group("ROLE_ACCOUNTANT"));
        } catch (Exception e) {

        }
    }
}
The ACME Security Department imposes the new security requirements for our service! They are based on the ASVS. Your task is to implement the following requirements from the V4. Access Control Verification Requirements paragraph:

Verify that all user and data attributes and policy information used by access controls cannot be manipulated by end users unless specifically authorized.
Verify that the principle of least privilege exists - users should only be able to access functions, data files, URLs, controllers, services, and other resources, for which they possess specific authorization. This implies protection against spoofing and elevation of privilege.
Verify that the principle of deny by default exists whereby new users/roles start with minimal or no permissions and users/roles do not receive access to new features until access is explicitly assigned.
Let's talk about roles. The Administrator is the user who registered first, all other users should receive the User role. The accountant role should be assigned by the Administrator to one of the users later.

To assign the roles and manage users, you will need to implement the following service endpoints:

PUT api/admin/user/role sets the roles;
DELETE api/admin/user deletes users;
GET api/admin/user obtains information about all users; the information should not be sensitive.
The roles should be divided into 2 groups: administrative (Administrator) and business users (Accountant, User).
Do not mix up the groups; a user can be either from the administrative or business group. A user with an administrative role should not have access to business functions and vice versa.

Objectives
Add the authorization to the service and implement the role model shown in the table above. The first registered user should receive the Administrator role, the rest — Users. In case of authorization violation, respond with HTTP Forbidden status (403) and the following body:

{
  "timestamp" : "<date>",
  "status" : 403,
  "error" : "Forbidden",
  "message" : "Access Denied!",
  "path" : "/api/admin/user/role"
}
Change the response for the POST api/auth/signup endpoint. It should respond with HTTP OK status (200) and the body with a JSON object with the information about a user. Add the roles field that contains an array with roles, sorted in ascending order:

{
   "id": "<Long value, not empty>",   
   "name": "<String value, not empty>",
   "lastname": "<String value, not empty>",
   "email": "<String value, not empty>",
   "roles": "<[User roles]>"
}
Add the GET api/admin/user endpoint. It must respond with an array of objects representing the users sorted by ID in ascending order. Return an empty JSON array if there's no information.

[
    {
        "id": "<user1 id>",
        "name": "<user1 name>",
        "lastname": "<user1 last name>",
        "email": "<user1 email>",
        "roles": "<[user1 roles]>"
    },
     ...
    {
        "id": "<userN id>",
        "name": "<userN name>",
        "lastname": "<userN last name>",
        "email": "<userN email>",
        "roles": "<[userN roles]>"
    }
]
Add the DELETE api/admin/user/{user email} endpoint, where {user email} specifies the user that should be deleted. The endpoint must delete the user and respond with HTTP OK status (200) and body like this:

{
   "user": "<user email>",
   "status": "Deleted successfully!"
}
If a user is not found, respond with HTTP Not Found status (404) and the following body:

{
    "timestamp": "<date>",
    "status": 404,
    "error": "Not Found",
    "message": "User not found!",
    "path": "<api + parameter>"
}
The Administrator should not be able to delete himself. In that case, respond with the HTTP Bad Request status (400) and the following body:

{
    "timestamp": "<date>",
    "status": 400,
    "error": "Bad Request",
    "message": "Can't remove ADMINISTRATOR role!",
    "path": "<api + path>"
}
Add the PUT api/admin/user/role endpoint that changes user roles. It must accept the following JSON body:

{
   "user": "<String value, not empty>",
   "role": "<User role>",
   "operation": "<[GRANT, REMOVE]>"
}
The operation field above determines whether the role will be provided or removed. If successful, respond with the HTTP OK status (200) and the body like this:

{
   "id": "<Long value, not empty>",   
   "name": "<String value, not empty>",
   "lastname": "<String value, not empty>",
   "email": "<String value, not empty>",
   "roles": "[<User roles>]"
}
In case of violation of the rules, the service must respond in the following way:

If a user is not found, respond with the HTTP Not Found status (404) and the following body:
{
    "timestamp": "<date>",
    "status": 404,
    "error": "Not Found",
    "message": "User not found!",
    "path": "/api/admin/user/role"
}
If a role is not found, respond with HTTP Not Found status (404) and the following body:
{
    "timestamp": "<date>",
    "status": 404,
    "error": "Not Found",
    "message": "Role not found!",
    "path": "/api/admin/user/role"
}
If you want to delete a role that has not been provided to a user, respond with the HTTP Bad Request status (400) and body like this:
{
    "timestamp": "<date>",
    "status": 400,
    "error": "Bad Request",
    "message": "The user does not have a role!",
    "path": "/api/admin/user/role"
}
If you want to remove the only existing role of a user, respond with the HTTP Bad Request status (400) and the following body:
{
    "timestamp": "<date>",
    "status": 400,
    "error": "Bad Request",
    "message": "The user must have at least one role!",
    "path": "/api/admin/user/role"
}
If you try to remove the Administrator, respond with the HTTP Bad Request status (400) and the following body:
{
    "timestamp": "<date>",
    "status": 400,
    "error": "Bad Request",
    "message": "Can't remove ADMINISTRATOR role!",
    "path": "/api/admin/user/role"
}
If an administrative user is granted a business role or vice versa, respond with the HTTP Bad Request status (400) and the following body:
{
    "timestamp": "<date>",
    "status": 400,
    "error": "Bad Request",
    "message": "The user cannot combine administrative and business roles!",
    "path": "/api/admin/user/role"
}
Examples
Example 1: a PUT request for /api/admin/user/role with the correct authentication under the Administrator role

Request:

{
   "user": "ivanivanov@acme.com",
   "role": "ACCOUNTANT",
   "operation": "GRANT"
}
Response: 200 OK

Response body:

{
    "id": 2,
    "name": "Ivan",
    "lastname": "Ivanov",
    "email": "ivanivanov@acme.com",
    "roles": [
        "ROLE_ACCOUNTANT",
        "ROLE_USER"
    ]
}
Example 2: a GET request for /api/empl/payment with the correct authentication under the Administrator role

Response: 200 OK

Response body:

[
    {
        "id": 1,
        "name": "John",
        "lastname": "Doe",
        "email": "johndoe@acme.com",
        "roles": [
            "ROLE_ADMINISTRATOR"
        ]
    },
    {
        "id": 2,
        "name": "Ivan",
        "lastname": "Ivanov",
        "email": "ivanivanov@acme.com",
        "roles": [
            "ROLE_ACCOUNTANT",
            "ROLE_USER"
        ]
    }
]
Example 3: a PUT request for /api/admin/user/role with the correct authentication under the Administrator role

Request:

{
   "user": "ivanivanov@acme.com",
   "role": "ACCOUNTANT",
   "operation": "REMOVE"
}
Response: 200 OK

Response body:

{
    "id": 2,
    "name": "Ivan",
    "lastname": "Ivanov",
    "email": "ivanivanov@acme.com",
    "roles": [
        "ROLE_USER"
    ]
}

Example 4: a GET request for /api/admin/user/role with the correct authentication under the User role

Response: 403 Forbidden

Response body:

{
    "timestamp": "<date>",
    "status": 403,
    "error": "Forbidden",
    "message": "Access Denied!",
    "path": "/api/admin/user/"
}

Example 5: a DELETE request for /api/admin/user/ivanivanov@acme.com with the correct authentication under the Administrator role

Response: 200 OK

Response body:

{
    "user": "ivanivanov@acme.com",
    "status": "Deleted successfully!"
}

Example 6: a DELETE request for /api/admin/user/johndoe@acme.com with the correct authentication under the Administrator role

Response: 400 Bad Request

Response body:

{
    "timestamp": "<date>",
    "status": 400,
    "error": "Bad Request",
    "message": "Can't remove ADMINISTRATOR role!",
    "path": "/api/admin/user/johndoe@acme.com"
}
Example 7: a DELETE request for /api/admin/user/ivanivanov@acme.com with the correct authentication under the Administrator role

Response: 404 Not Found

Response body:

{
    "timestamp": "<date>",
    "status": 404,
    "error": "Not Found",
    "message": "User not found!",
    "path": "/api/admin/user/ivanivanov@acme.com"
}

Work on project. Stage 6/7: Logging events

Description
The security department has put forward new requirements. The service must log information security events. Take a look at what they include:

Description	Event Name
A user has been successfully registered	CREATE_USER
A user has changed the password successfully	CHANGE_PASSWORD
A user is trying to access a resource without access rights	ACCESS_DENIED
Failed authentication	LOGIN_FAILED
A role is granted to a user	GRANT_ROLE
A role has been revoked	REMOVE_ROLE
The Administrator has locked the user	LOCK_USER
The Administrator has unlocked a user	UNLOCK_USER
The Administrator has deleted a user	DELETE_USER
A user has been blocked on suspicion of a brute force attack	BRUTE_FORCE
The composition of the security event fields is presented below:

{
    "date": "<date>",
    "action": "<event_name from table>",
    "subject": "<The user who performed the action>",
    "object": "<The object on which the action was performed>",
    "path": "<api>"
}
If it is impossible to determine a user, output Anonymous in the subject field. All examples of events are provided in the Examples.

Also, you need to add the role of the auditor. The auditor is an employee of the security department who analyzes information security events and identifies incidents. You need to add the appropriate endpoint for this. A user with the auditor role should be able to receive all events using the endpoint. The auditor is a part of the business group. We suggest that you implement the storage of information security events in the database, although you can choose another solution. Make sure it is persistent.

Let's also discuss what a security incident is. For example, if a user made a mistake in entering a password. This is a minor user error, but numerous repeated attempts to log in with the wrong password can be evidence of a brute-force attack. In this case, it is necessary to register the incident and conduct an investigation. Information security events are collected in our service to serve as a basis for identifying incidents in the future after transmission to the Security Information and Event Management systems (SIEM).

Let's implement a simple rule for detecting a brute force attack. If there are more than 5 consecutive attempts to enter an incorrect password, an entry about this should appear in the security events. Also, the user account must be blocked.

To unlock a user, you will need to add a new administrative endpoint: api/admin/user/access.

Tip: The following articles can help you with these tasks: Prevent Brute Force Authentication Attempts with Spring Security by Baeldung and Spring Security Limit Login Attempts Example by CodeJava.

Objectives
Implement logging security events in the application following the requirements described above.

Implement a mechanism to block the user after 5 consecutive failed logins. In a case like this, the next events should be logged: LOGIN_FAILED -> BRUTE_FORCE -> LOCK_USER. In case of a successful login, reset the counter of the failed attempt.

Add the PUT api/admin/user/access endpoint that locks/unlocks users. It accepts the following JSON body:

{
   "user": "<String value, not empty>",
   "operation": "<[LOCK, UNLOCK]>" 
}
Where operation determines whether the user will be locked or unlocked. If successful, respond with the HTTP OK status (200) and the following body:

{
    "status": "User <username> <[locked, unlocked]>!"
}
For safety reasons, the Administrator cannot be blocked. In this case, respond with the HTTP Bad Request status (400) and the following body:

{
    "timestamp": "<date>",
    "status": 400,
    "error": "Bad Request",
    "message": "Can't lock the ADMINISTRATOR!",
    "path": "<api>"
}
For other errors, return responses like in the previous stage.

Add the GET api/security/events endpoint that must respond with an array of objects representing the security events of the service sorted in ascending order by ID. If no data is found, the service should return an empty JSON array.

[
    {
        "date": "<date>",
        "action": "<event_name for event1>",
        "subject": "<The user who performed the action>",
        "object": "<The object on which the action was performed>",
        "path": "<api>"
    },
     ...
    {
        "date": "<date>",
        "action": "<event_name for eventN>",
        "subject": "<The user who performed the action>",
        "object": "<The object on which the action was performed>",
        "path": "<api>"
    }
]
Update the role model:

Anonymous	User	Accountant	Administrator	Auditor
POST api/auth/signup	+	+	+	+	+
POST api/auth/changepass		+	+	+	-
GET api/empl/payment	-	+	+	-	-
POST api/acct/payments	-	-	+	-	-
PUT api/acct/payments	-	-	+	-	-
GET api/admin/user	-	-	-	+	-
DELETE api/admin/user	-	-	-	+	-
PUT api/admin/user/role	-	-	-	+	-
PUT api/admin/user/access	-	-	-	+	-
GET api/security/events	-	-	-	-	+
Examples
Example 1: a GET request for api/auth/signup under the Auditor role

Response: 200 OK

Request body:

[
{
  "id" : 1,
  "date" : "<date>",
  "action" : "CREATE_USER",
  "subject" : "Anonymous", \\ A User is not defined, fill with Anonymous
  "object" : "johndoe@acme.com",
  "path" : "/api/auth/signup"
}, {
  "id" : 6,
  "date" : "<date>",
  "action" : "LOGIN_FAILED",
  "subject" : "maxmustermann@acme.com",
  "object" : "/api/empl/payment", \\ the endpoint where the event occurred
  "path" : "/api/empl/payment"
}, {
  "id" : 9,
  "date" : "<date>",
  "action" : "GRANT_ROLE",
  "subject" : "johndoe@acme.com",
  "object" : "Grant role ACCOUNTANT to petrpetrov@acme.com",
  "path" : "/api/admin/user/role"
}, {
  "id" : 10,
  "date" : "<date>",
  "action" : "REMOVE_ROLE",
  "subject" : "johndoe@acme.com",
  "object" : "Remove role ACCOUNTANT from petrpetrov@acme.com",
  "path" : "/api/admin/user/role"
}, {
  "id" : 11,
  "date" : "<date>",
  "action" : "DELETE_USER",
  "subject" : "johndoe@acme.com",
  "object" : "petrpetrov@acme.com",
  "path" : "/api/admin/user"
}, {
  "id" : 12,
  "date" : "<date>",
  "action" : "CHANGE_PASSWORD",
  "subject" : "johndoe@acme.com",
  "object" : "johndoe@acme.com",
  "path" : "/api/auth/changepass"
}, {
  "id" : 16,
  "date" : "<date>",
  "action" : "ACCESS_DENIED",
  "subject" : "johndoe@acme.com",
  "object" : "/api/acct/payments", \\ the endpoint where the event occurred
  "path" : "/api/acct/payments"
}, {
  "id" : 25,
  "date" : "<date>",
  "action" : "BRUTE_FORCE",
  "subject" : "maxmustermann@acme.com",
  "object" : "/api/empl/payment", \\ the endpoint where the event occurred
  "path" : "/api/empl/payment"
}, {
  "id" : 26,
  "date" : "<date>",
  "action" : "LOCK_USER",
  "subject" : "maxmustermann@acme.com",
  "object" : "Lock user maxmustermann@acme.com",
  "path" : "/api/empl/payment" \\ the endpoint where the lock occurred
}, {
  "id" : 27,
  "date" : "<date>",
  "action" : "UNLOCK_USER",
  "subject" : "johndoe@acme.com",
  "object" : "Unlock user maxmustermann@acme.com",
  "path" : "/api/admin/user/access"
}
]
Example 2: a POST request for /api/admin/user/role

Request body:

{
   "user": "administrator@acme.com",
   "role": "AUDITOR",
   "operation": "GRANT" 
}
Response: 400 Bad Request

Response body:

{
    "timestamp": "<date>",
    "status": 400,
    "error": "Bad Request",
    "message": "The user cannot combine administrative and business roles!",
    "path": "/api/admin/user/role"
}
Example 3: a PUT request for PUT api/admin/user/access

Request body:

{
   "user": "administrator@acme.com",
   "operation": "LOCK" 
}
Response: 400 Bad Request

Response body:

{
    "timestamp": "<date>",
    "status": 400,
    "error": "Bad Request",
    "message": "Can't lock the ADMINISTRATOR!",
    "path": "/api/admin/user/access"
}
Example 4: a PUT request for PUT api/admin/user/access

Request body:

{
   "user": "user@acme.com",
   "operation": "LOCK" 
}
Response: 200 OK

Response body:

{
    "status": "User user@acme.com locked!"
}
