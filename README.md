This project is a **Spring Boot RESTful API** that supports:
- ✅ Multi-company (multitenancy)
- ✅ Role-based access control (RBAC)
- ✅ Centralized permission management
- ✅ JWT authentication & company selection flow

---

🔑 Default Super Admin Account:
Email: superadmin
Password: 123456


## 🚀 Getting Started (Workflow)

### 1. Login
POST /api/auth/login
Input: { "username": "", "password": "" }

Response: List of companies the user belongs to.

2. Select Company
POST /api/auth/select-company
Input: { "companyId": "" }

Response: Returns a JWT token containing:

companyId

roles

permissions

💡 Use this token in all following requests as:
Authorization: Bearer <your_token_here>

🔐 Authentication API
Method	Endpoint	Description	Role Required
POST	/api/auth/login	Login and return list of companies	❌ Public
POST	/api/auth/select-company	Select company, return JWT + roles + permissions	✅ Any role in selected company

🏢 Company Management (/api/companies)
🔒 Only for SUPER_ADMIN

Method	Endpoint	Description
GET	/api/companies	Get all companies
POST	/api/companies	Create a new company
GET	/api/companies/{id}	Get company by ID
PUT	/api/companies/{id}	Update company
DELETE	/api/companies/{id}	Delete company

👤 User Management (/api/users)
Method	Endpoint	Description	Role Required	Note
GET	/api/users	Get users in current company	SUPER_ADMIN, ADMIN, MANAGER	
GET	/api/users/{id}	Get user details	isAuthenticated()	Deny if lower role
POST	/api/users	Create a new user in company	ADMIN	
PUT	/api/users/{id}	Update user info	isAuthenticated()	Deny if lower role
DELETE	/api/users/{id}	Delete user	isAuthenticated()	Deny if lower role

👥 Employee Management (/api/employees)
Method	Endpoint	Description	Role Required
GET	/api/employees	Get all employees in company	ADMIN, MANAGER
GET	/api/employees/{id}	Get employee details by ID	ADMIN, MANAGER, STAFF
POST	/api/employees	Create employee (default role = STAFF)	ADMIN, MANAGER, USER
PUT	/api/employees/{id}	Update employee info	ADMIN, MANAGER, STAFF
DELETE	/api/employees/{id}	Delete employee	ADMIN, MANAGER

🛡️ Super Admin Panel (/api/superadmin)
🔒 Only for SUPER_ADMIN

Method	Endpoint	Description
GET	/api/superadmin/users	Get all system users
POST	/api/superadmin/users	Create new system user
GET	/api/superadmin/roles	Get all roles
POST	/api/superadmin/roles	Create a new role
PUT	/api/superadmin/roles/{id}	Update a role
DELETE	/api/superadmin/roles/{id}	Delete a role
PUT	/api/superadmin/roles/{roleId}/permissions	Assign permissions to role
GET	/api/superadmin/permissions	Get all permissions
POST	/api/superadmin/permissions	Create new permission
GET	/api/superadmin/summary	System overview (users, roles, permissions)

🔑 Token Payload (after company selection)

{
  "sub": "username",
  "companyId": "uuid",
  "roles": ["ADMIN", "MANAGER"],
  "permissions": ["CREATE_USER", "VIEW_EMPLOYEE"],
  "exp": 1234567890
}
Use this token to:

Check permissions in frontend (show/hide UI features)

Protect routes on backend (@PreAuthorize)

🧩 Role Hierarchy (Example)

SUPER_ADMIN
  ├── Can access all companies & system configs
ADMIN
  ├── Full control within their company
MANAGER
  ├── Manage employees, view users
STAFF
  ├── View own profile
🛠️ Backend Tech Stack
Java Spring Boot

Spring Security (JWT + Role/Permission)

JPA + PostgreSQL/MongoDB

DTO + Entity + Mapper pattern

Multi-tenant & RBAC architecture

📌 Notes
All APIs after login require Authorization: Bearer {token}

Only users associated with selected companyId can proceed

Role-permission mapping is customizable via /api/superadmin/roles/{id}/permissions

📂 Directory Structure (Backend)

src/
├── config/
├── controller/
├── dto/
├── entity/
├── mapper/
├── repository/
├── security/
├── service/
└── ...
🧪 Postman Collection
Coming soon...

👨‍💻 Author
Nguyễn Tấn Phúc
nguyentannphuc@gmail.com
