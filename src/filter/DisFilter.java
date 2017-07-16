package filter;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet Filter implementation class DisFilter
 */
@WebFilter(
		urlPatterns = { "/DisServlet" }, 
		initParams = { 
				@WebInitParam(name = "path", value = "c:")
		})
public class DisFilter implements Filter {

	
	private List<String> dirtyData;
    /**
     * Default constructor. 
     */
    public DisFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		final HttpServletRequest request1 = (HttpServletRequest) request;    
		HttpServletResponse response1 = (HttpServletResponse) response;
		
		System.out.println("the filter in doFilter function working ");
		// һ��������ҵ��
		request1.setCharacterEncoding("UTF-8");					// POST�ύ��Ч
		response1.setContentType("text/html;charset=UTF-8");
		
		/*
		 * ����GET�������룬����Ϊ��request.getParameter�����ڲ�û�н����ύ��ʽ�жϲ�����
		 * String name = request.getParameter("userName");
		 * 
		 * �������ָ���ӿڵ�ĳһ���������й�����չ������ʹ�ô���!
		 *      ��request����(Ŀ�����)�������������
		 */
		System.out.println("the filter in doFilter function works ");
		HttpServletRequest proxy =  (HttpServletRequest) Proxy.newProxyInstance(
				request1.getClass().getClassLoader(), 		// ָ����ǰʹ�õ��ۼ�����
				new Class[]{HttpServletRequest.class}, 		// ��Ŀ�����ʵ�ֵĽӿ�����
				new InvocationHandler() {					// �¼�������
					@Override
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						// ���巽������ֵ
						Object returnValue = null;
						// ��ȡ������
						String methodName = method.getName();
						// �жϣ���getParameter��������GET�ύ���Ĵ���
						if ("getParameter".equals(methodName)) {
							
							// ��ȡ��������ֵ�� <input type="text" name="userName">��
							String value = request1.getParameter(args[0].toString());	// ����Ŀ�����ķ���
							System.out.println("ԭ����value"+value);
							// ��ȡ�ύ��ʽ
							String methodSubmit = request1.getMethod(); // ֱ�ӵ���Ŀ�����ķ���
							
							// �ж������GET�ύ����Ҫ�����ݽ��д���  (POST�ύ�Ѿ��������)
							if ("GET".equals(methodSubmit)) {
								System.out.println("�ܽ������1��");
								if (value != null && !"".equals(value.trim())){
									// ����GET����
//									value = new String(value.getBytes("ISO8859-1"),"UTF-8");
									System.out.println("�ܽ������2��");
									System.out.println("�µĵ�value"+value);
								}
							} 
							for(String dirtyword:dirtyData)
							{
								if(value.contains(dirtyword))
								{
									value=value.replace(dirtyword, "xxxx");
								}
								
							}
							
							
							return value;
						}
						else {
							// ִ��request�������������
							returnValue = method.invoke(request1, args);
						}
						
						return returnValue;
					}
				});
		
		// �������� (ִ����һ������������servlet)

		// pass the request along the filter chain
		chain.doFilter(proxy, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		dirtyData=new ArrayList<>();
		dirtyData.add("ɵ��");
		dirtyData.add("������");
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	 System.out.println("have fun");
	
	 
	}

}
