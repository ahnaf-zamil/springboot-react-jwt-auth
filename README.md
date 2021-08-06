# Spring Boot JWT Authentication with React

This is a simple web application which uses JWT authentication with React and Spring Boot.
I made this because I was bored, but also because it might help you out.

## Technologies used
- React.js
- Spring Boot (Java 8)
- MySQL 5.7
- Nginx
- Docker

## How to run

Make sure you have JDK 8 and Node.js installed.

Rename `.env.example` to `.env` and fill in the credentials

Clone the repo

```bash
git clone https://github.com/ahnaf-zamil/springboot-react-jwt-auth
cd springboot-react-jwt-auth/
```

Build the API

```bash
cd api/
./mvnw package -Dmaven.test.skip=true # You probably might have to use chmod +x ./mvnw if you are on linux
```

Build the frontend

```bash
cd web/
npm install # or yarn install
npm run build # or yarn build
```

Run the composite

```bash
docker-compose up -d --build
```

Now visit `localhost:9090` and you will see the React frontend. 

The Spring Boot API is accessible at `localhost:9090/api`, as it is reverse-proxied using Nginx.

## License

This is licensed under the MIT License. Check out the [LICENSE](./LICENSE) file.
