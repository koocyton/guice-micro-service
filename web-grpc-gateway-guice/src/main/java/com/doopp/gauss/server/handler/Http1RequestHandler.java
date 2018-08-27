package com.doopp.gauss.server.handler;

import com.doopp.gauss.server.dispatcher.RequestDispatcher;
import com.doopp.gauss.server.filter.SessionFilter;
import com.google.inject.Inject;
import com.google.inject.Injector;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

public class Http1RequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Inject
    private RequestDispatcher requestDispatcher;

    @Inject
    private Injector injector;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {

        if (HttpUtil.is100ContinueExpected(httpRequest)) {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
            ctx.writeAndFlush(response);
        }

        FullHttpResponse httpResponse = new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.OK);
        // httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

<<<<<<< HEAD
        // RequestDispatcher

        (new RequestDispatcher())
            // set filter
            .filter(new SessionFilter())
            .context(ctx)
            .request(httpRequest)
            .response(httpResponse)
            .injector(injector)
            .dispatcher();
=======
        requestDispatcher.processor(ctx, httpRequest, httpResponse);
        // Filter
        SessionFilter sessionFilter = injector.getInstance(SessionFilter.class);

        // Dispatch
        if (sessionFilter.doFilter()) {
            injector.getInstance(RequestDispatcher.class).processor(ctx, httpRequest, httpResponse);
        }
>>>>>>> 1de8f7181667766ed4e030d20cce9e9c0e7f3afe

        httpResponse.headers().set(CONTENT_LENGTH, httpResponse.content().readableBytes());

        if (HttpUtil.isKeepAlive(httpRequest)) {
            httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        ctx.write(httpResponse);
        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!HttpUtil.isKeepAlive(httpRequest)) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
