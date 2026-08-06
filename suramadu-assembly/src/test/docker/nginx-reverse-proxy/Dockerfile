 
FROM manticore-projects/suramadu

RUN apt-get update && apt-get install -y \
    nginx \
    git \
    maven
    
RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*


# forward request and error logs to docker log collector
RUN ln -sf /dev/stdout /var/log/nginx/access.log \
	&& ln -sf /dev/stderr /var/log/nginx/error.log

EXPOSE 80 443

COPY nginx.conf /etc/nginx/nginx.conf

RUN mkdir /etc/service/nginx && \
    echo "#!/bin/sh\nexec nginx -g \"daemon off;\"" > /etc/service/nginx/run && \
    chmod +x /etc/service/nginx/run

#RUN rm /etc/service/suramadu/run

ENV JAVA_HOME=/opt/java/openjdk