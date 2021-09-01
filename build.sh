readonly TAG=$1
readonly DOCKER_IMAGE=nachocode/file-api-http4s
sbt assembly && docker build -t "$DOCKER_IMAGE:$TAG" .
