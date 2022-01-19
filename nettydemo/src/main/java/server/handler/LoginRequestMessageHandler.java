package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.LoginRequestMessage;
import message.LoginResponseMessage;
import server.service.UserService;
import server.service.UserServiceFactory;
import server.session.SessionFactory;

/**
 * @author zcl
 * @date 2022/1/12 16:31
 */
@ChannelHandler.Sharable
@Slf4j
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        String username = msg.getUsername();
        String password = msg.getPassword();
        log.debug("server receive login msg username:{} password:{}",username,password);
        UserService userService = UserServiceFactory.getUserService();
        boolean login = userService.login(username, password);
        LoginResponseMessage message;
        if (login) {
            SessionFactory.getSession().bind(ctx.channel(), username);
            message = new LoginResponseMessage(true, "登陆成功");
        } else {
            message = new LoginResponseMessage(false, "用户名或密码错误");
        }
        log.debug("server response login result:{}",message);
        ctx.writeAndFlush(message);
    }
}
