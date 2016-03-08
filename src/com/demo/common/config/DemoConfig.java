package com.demo.common.config;

import com.demo.blog.BlogController;
import com.demo.common.model._MappingKit;
import com.demo.index.IndexController;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

/**
 * API引导式配置
 */
public class DemoConfig extends JFinalConfig {
	//要继承jfinal的JFinalConfig
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用PropKit.get(...)获取值
		PropKit.use("a_little_config.txt");//可以跨越包进行文件获取吗，还是其他地方有配置？（上面一句是解释，默认自动搜索）
		me.setDevMode(PropKit.getBoolean("devMode", false));//啥意思？
		//me.setViewType(ViewType.JSP);                           
		// 设置视图类型为Jsp，否则默认为FreeMarker
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {//路由配置有什么作用，如何用？（设定根目录，和其他目录，是对应html文件的视图的根目录）
		me.add("/", IndexController.class, "/index");	// 第三个参数为该Controller的视图存放路径
		me.add("/blog", BlogController.class);			// 第三个参数省略时默认与第一个参数值相同，在此即为 "/blog"
	}//斜杠/也就是根目录WebRoot
	
	public static C3p0Plugin createC3p0Plugin() {//什么用？帮助数据库连接池获取参数？
		return new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		C3p0Plugin C3p0Plugin = createC3p0Plugin();
		me.add(C3p0Plugin);
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(C3p0Plugin);
		me.add(arp);
		
		// 所有配置在 MappingKit 中搞定
		_MappingKit.mapping(arp);
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		
	}
	
	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	//jfinal设定好会在整个项目找？
	public static void main(String[] args) {
		JFinal.start("WebRoot", 80, "/", 5);
		//主函数设定项目运行根目录，端口号，刷新频率
	}
}
