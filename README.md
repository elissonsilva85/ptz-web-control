# PTZ Web Control

This is a web-based PTZ (Pan, Tilt, Zoom) control project, developed with Spring Boot (backend) and Angular (frontend).

## Project Structure

The project is organized into two main parts:

### Backend (`/backend`)
- Developed in Java 17
- Uses Spring Boot
- Dependency management with Maven
- Contains all PTZ control logic and camera communication
- H2 database for local storage

### Frontend (`/frontend`)
- Developed in Angular
- Web interface for camera control
- Virtual joystick system
- LED status indicators
- Proxy configuration for development

## Prerequisites

To compile and run the project, you'll need to have installed:

- JDK 17 or higher
- Maven 3.6 or higher
- Node.js 16 or higher
- NPM (usually comes with Node.js)
- Angular CLI (`npm install -g @angular/cli`)

## How to Build

### Building the Backend

1. In the project root, run:
   ```bash
   mvn clean install
   ```
   
   This will build both the backend and frontend.

### Building Frontend Only (for development)

1. Navigate to the frontend folder:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Build the project:
   ```bash
   npm run build
   ```

## How to Run

### Running in Production Mode

1. After building the complete project, navigate to the `backend/target` folder:
   ```bash
   cd backend/target
   ```

2. Run the JAR file:
   ```bash
   java -jar ptzWebControl.jar
   ```

3. Access the application in your browser:
   ```
   http://localhost:8080
   ```

### Running in Development Mode

1. Backend:
   - In the `backend` folder, run:
     ```bash
     mvn spring-boot:run
     ```

2. Frontend:
   - In another terminal, in the `frontend` folder, run:
     ```bash
     npm run start:proxy
     ```
   - The frontend will start and automatically open in your default browser
   - For development, the frontend will be available at:
     ```
     http://localhost:4200
     ```

## Configuration

- The backend configuration file is in `backend/src/main/resources/application.yaml`
- Development configurations are in `application-dev.yaml`
- Development proxy configurations are in `frontend/proxy.conf.json`

## Logs

- Backend logs are generated in the `backend/logs` folder
- Frontend logs are generated in `frontend/error.log`

## Notes

- The H2 database is automatically created in the `backend/data` folder
- Production and development environment configurations can be adjusted in their respective configuration files
- For development, the proxy is configured to redirect frontend API calls to the backend

## Contributing

1. Fork the project
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -m 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Open a Pull Request

## License

This project is under the [MIT](LICENSE) license.