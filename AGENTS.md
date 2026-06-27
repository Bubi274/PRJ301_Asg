# AGENTS.md

## Purpose

This file defines the rules that any AI coding assistant must follow when working on this project.

The project must follow the school project requirements exactly. Do not introduce technologies, structures, or coding patterns that violate these rules.

---

## 1. Mandatory Technology Stack

Use only the following technologies:

- Java Servlet
- JSP
- JSTL and Expression Language (EL)
- JDBC
- SQL Server
- HTML
- CSS
- JavaScript
- Apache Tomcat

Allowed UI support:

- Plain CSS
- Bootstrap, if the project already uses it or the user requests it

Do not use or add:

- Spring Framework
- Spring Boot
- Maven
- Gradle
- Hibernate
- JPA
- Thymeleaf
- JSF
- Angular
- React
- Vue
- Any front-end framework
- Any ORM framework
- Any dependency or architecture that changes the project away from Servlet/JSP/JDBC MVC

If a requested solution seems to require a prohibited technology, implement the same feature using Servlet, JSP, JSTL/EL, JDBC, and SQL Server instead.

---

## 2. Architecture Rules

The project must follow Pure MVC architecture.

Required separation:

- Controller: Java Servlet classes only
- Model: Java Bean, DTO, Entity classes
- DAO/DAL: Database access logic
- View: JSP pages
- Utils: Common helper classes such as database connection, validation, constants
- Filter: Authentication and authorization filters

Business logic must not be placed inside JSP pages.

JSP pages are only for displaying data and submitting forms.

Controllers must:

- Receive requests
- Validate basic request flow
- Call DAO or Service classes
- Store data in request/session attributes
- Forward or redirect to JSP pages

DAO classes must:

- Contain SQL queries
- Use JDBC
- Use PreparedStatement
- Map ResultSet data to model objects
- Close database resources safely

Model classes must:

- Be simple JavaBeans
- Use private fields
- Provide constructors, getters, and setters
- Avoid database logic and request/response logic

Filters must:

- Protect pages by login status
- Protect pages by user role
- Redirect unauthorized users properly

---

## 3. Recommended Project Structure

Follow the existing project structure. If new files are needed, place them in the correct package or folder.

Recommended structure:

```text
src
├── controller
├── dao
├── dto
├── model
├── utils
└── filter

web
├── jsp
├── css
├── js
└── images
```

If the project already uses a different but similar structure, keep the existing structure and remain consistent with it.

Do not rename packages, folders, database tables, or existing classes unless the user explicitly asks.

Do not rewrite the whole project when only a small feature or bug fix is requested.

---

## 4. Database and JDBC Rules

The project must connect to Microsoft SQL Server using JDBC.

Always use:

- Connection Pool through `DBCPUtils` or the existing connection utility
- `PreparedStatement` for all SQL statements
- Primary keys and foreign keys in database design
- Normalized database design up to Third Normal Form (3NF)

Never use:

- String concatenation for SQL values
- Raw user input directly inside SQL queries
- Hibernate, JPA, or ORM tools

Example of correct SQL handling:

```java
String sql = "Select * From Users Where Email = ? And Password = ?";
PreparedStatement ps = connection.prepareStatement(sql);
ps.setString(1, email);
ps.setString(2, password);
```

Example of incorrect SQL handling:

```java
String sql = "Select * From Users Where Email = '" + email + "'";
```

All database code must close `Connection`, `PreparedStatement`, and `ResultSet` safely using try-with-resources when possible.

---

## 5. SQL Style Rules

When writing SQL code, follow the user's preferred style:

- Use Title Case for SQL keywords
- Do not write SQL keywords in all uppercase
- Use readable names
- Keep SQL simple and suitable for a student Servlet/JSP/JDBC project

Preferred style:

```sql
Create Table Users (
    UserID Int Primary Key,
    FullName Nvarchar(100),
    Email Varchar(100)
);
```

Avoid this style:

```sql
CREATE TABLE USERS (
    USER_ID INT PRIMARY KEY
);
```

---

## 6. Authentication and Authorization

The system must support at least 3 user roles.

Typical roles:

- Admin
- Staff or Employee
- Customer or User

For hotel management projects, use roles such as:

- Admin
- Staff
- Tenant or Guest

Authentication requirements:

- Login must use session-based authentication
- Store logged-in user information in `HttpSession`
- Do not store sensitive user information in cookies
- Logout must invalidate the session
- Protected pages must not be accessible after logout

Authorization requirements:

- Use Authorization Filter
- Check user role before allowing access to protected features
- Redirect unauthenticated users to login page
- Show an error page or redirect unauthorized users safely

---

## 7. CRUD Requirements

The project must include at least 5 core business entities or tables.

Each core entity should support full CRUD when appropriate:

- Create
- Read/List/Detail
- Update
- Delete

When implementing CRUD:

- Use Servlet for controller logic
- Use DAO for database logic
- Use JSP for forms and tables
- Validate input before inserting or updating
- Check duplicate data when required
- Show success and error messages clearly
- Keep redirect/forward flow consistent with the existing project

Do not put insert, update, delete, or select SQL inside JSP files.

---

## 8. Search Requirements

The system must support search functionality.

Include:

- Keyword-based search
- Advanced search with multiple criteria when the feature requires it

Common search criteria:

- Name
- ID or Code
- Date
- Status
- Type
- Role

Search must be implemented through DAO methods using `PreparedStatement`.

Do not build dynamic SQL unsafely with raw user input.

---

## 9. Pagination Requirements

List pages should support pagination when data can grow large.

Pagination should include:

- Previous
- Page numbers
- Next

Implementation rules:

- Use page index and page size
- Validate page index
- Keep search filters when moving between pages
- Use SQL Server pagination when possible

Example SQL Server pagination pattern:

```sql
Order By CreatedAt Desc
Offset ? Rows Fetch Next ? Rows Only
```

---

## 10. Validation Rules

All forms must validate input.

Required validation types:

- Required field validation
- Format validation
- Duplicate data validation

Common format validation:

- Email
- Phone number
- Date
- Number range
- Status value
- Role value

Validation should happen before database changes.

Recommended validation flow:

1. Read request parameters in Servlet.
2. Trim input strings.
3. Check empty fields.
4. Check format.
5. Check duplicate data through DAO if needed.
6. If invalid, forward back to JSP with error message and old input.
7. If valid, call DAO to save data.

Do not rely only on HTML validation. Server-side validation is required.

---

## 11. Reports and Statistics

The project must include at least 5 reports or statistics pages.

Possible report examples:

- Monthly Revenue Report
- Top-Selling Products or Services
- Inventory Report
- VIP Customers
- Orders by Status
- Bookings by Status
- Room Occupancy Report
- Service Request Statistics
- Revenue by Room Type
- Staff Performance Report

Report rules:

- Report data must come from SQL Server through DAO methods
- Use aggregate SQL queries such as `Count`, `Sum`, `Group By`, and `Order By`
- Display reports clearly in JSP
- Charts may use simple JavaScript if already available
- Keep report logic out of JSP

---

## 12. Security Rules

Mandatory security requirements:

- Session-based login
- Logout functionality
- Authorization filter
- SQL Injection prevention using `PreparedStatement`

Additional security rules:

- Do not expose stack traces to users
- Do not print passwords
- Do not store plain text passwords if the existing project already supports hashing
- Do not allow users to access another user's data without role permission
- Validate IDs from request parameters
- Check ownership before showing user-specific data
- Use POST for create, update, delete actions
- Use GET mainly for list, search, and detail pages

---

## 13. User Interface Rules

The UI must be simple, clear, and consistent.

Required UI qualities:

- Basic responsive design
- Clear navigation menu
- Consistent layout
- Consistent button style
- Clear form labels
- Clear error and success messages

JSP rules:

- Use JSTL and EL for displaying data
- Avoid Java scriptlet code inside JSP
- Use shared header, footer, sidebar, or navbar if the project already has them
- Keep JSP focused on view only

Example:

```jsp
<c:forEach var="item" items="${items}">
    <tr>
        <td>${item.name}</td>
        <td>${item.status}</td>
    </tr>
</c:forEach>
```

Avoid:

```jsp
<%
    // Java business logic here
%>
```

---

## 14. File Upload, Export, AJAX, and Bonus Features

Bonus features are allowed only if they do not violate the mandatory technology stack.

Possible bonus features:

- Image Upload
- Excel Export
- PDF Export
- Interactive Statistical Dashboard
- AJAX
- Email Verification
- Third-party API Integration
- System Logging

Rules for bonus features:

- Do not add Maven or Gradle just to support a bonus feature
- Do not add Spring or ORM frameworks
- Prefer simple Servlet/JSP/JDBC compatible implementation
- Ask the user before adding large external libraries
- Keep bonus features optional and separate from core features

---

## 15. Coding Behavior for AI Assistant

Before coding, the AI assistant must:

1. Read the existing project structure.
2. Identify the current packages, naming conventions, and database utilities.
3. Follow the existing coding style.
4. Make the smallest safe change needed.
5. Avoid rewriting unrelated files.
6. Avoid changing database schema unless the user asks.
7. Explain which files were changed after coding.

When creating a new feature, the AI assistant must create or update:

- Servlet controller
- DAO method
- Model or DTO if needed
- JSP page or JSP section
- Validation logic
- Navigation link if needed
- SQL script if database change is required
- Filter rule if authorization is required

When fixing a bug, the AI assistant must:

- Identify the root cause
- Fix only the related code
- Preserve existing behavior
- Mention how to test the fix

---

## 16. Do Not Do These Actions

Do not:

- Convert the project to Spring Boot
- Add Maven or Gradle
- Add Hibernate or JPA
- Put SQL inside JSP
- Put business logic inside JSP
- Bypass authentication or authorization
- Use raw SQL string concatenation with user input
- Remove existing working features
- Rename existing tables or fields without permission
- Rewrite the whole project without permission
- Delete files without permission
- Change UI design drastically unless requested
- Add unsupported dependencies
- Generate code that cannot run on Apache Tomcat

---

## 17. Definition of Done

A task is only done when:

- The project still follows Servlet/JSP/JDBC MVC
- Code compiles
- Feature can run on Apache Tomcat
- DAO uses `PreparedStatement`
- JSP does not contain business logic
- Validation is included
- Role/session checks are respected
- CRUD/search/pagination/report rules are followed when relevant
- UI is consistent with the current project
- No prohibited technology is added
- Changed files are listed clearly

---

## 18. Response Format for AI Assistant

After completing a coding task, respond using this format:

```text
Done.

Changed files:
- path/to/File1.java
- path/to/File2.jsp

What was implemented:
- Short summary of the feature or fix

How to test:
1. Step one
2. Step two
3. Expected result

Notes:
- Any important warning or database script needed
```

Keep explanations short and practical.

---

## 19. Priority Order

If there is any conflict, follow this priority:

1. User's direct instruction in the current prompt
2. This `AGENTS.md` file
3. Existing project structure and coding style
4. General best practices

Never violate the mandatory school requirements.
