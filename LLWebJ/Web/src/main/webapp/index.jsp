<html>
<body>
<h2>Hello World!</h2>
<p>
This is <code>index.jsp</code>.
</p>
<p>
This is a list of working servlets so far:
</p>
<p>
      <!-- These links can't be absolute because the context part (the name of the war file) will be lost. -->
  <table>
    <tr>
      <td><a href="hello">HelloServlet</a></td>
      <td>Just a dumb servlet that immediately routes back to this page.  You'll see the URL change in the navigation
            bar.</td>
    </tr>
    <tr>
      <td><a href="json">JsonServlet</a></td>
      <td>Dumb servlet that just spits out some JSON w/out routing back to a JSP.</td>
    </tr>
    <tr>
      <td><a href="hello-cdi">HelloCdiServlet</a></td>
      <td>Servlet that uses CDI to inject a bean.  Also does not route back to a JSP, so there's no EL fooferaw.</td>
    </tr>
    <tr>
      <td><a href="members">Members</a></td>
      <td>Servlet that queries a JDBC database using a CDI bean, and dumps the results to the output stream as JSON.
      </td>
    </tr>
    <tr>
      <td><a href="jndiDump">JNDI Dump</a></td>
      <td>Servlet dumps out JNDI environment</td>
    </tr>
  </table>
</p>
</body>
</html>
