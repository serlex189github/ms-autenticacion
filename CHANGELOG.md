## [1.0.0] - 2025-08-27
### ✨ Features
- feat(auth): endpoint POST /api/v1/usuarios con validaciones y persistencia transaccional

### 🛠️ Improvements
- refactor: separación estricta hexagonal (api, domain, infrastructure)

### 🐛 Fixes
- fix(validation): rango salario_base (0–15000000) y formato email

### 🔒 Reliability
- feat(registro): Registro de usuario
- feat(registro): Validaciones
- feat(errors): manejo global de excepciones → respuestas controladas (400/409/500)

**Notas:** primera versión productiva del MS Autenticación.
