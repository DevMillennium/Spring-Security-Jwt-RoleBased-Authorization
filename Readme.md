# Spring-Security-Jwt

### Başlamadan Önce 

<p> Başlamadan önce Birkaç terime hakim olmamız gerekiyor konuyu daha iyi anlamak için </p>

- <strong> Authentication </strong>  : <p> Sağlanan kimlik bilgilerine dayalı olarak bir kullanıcının kimliğini doğrulama sürecini ifade eder. Yaygın bir örnek olarak web sitesinde oturum açtığınızda bir kullanıcı adı ve şifre girmektir .Sen Kimsin sorusuna cevap olarak düşünebiliriz  </p>

- <strong> Authorization </strong>  : <p> Kullanıcının kimliğinin başarıyla doğrulandığını varsayarak , bir kullanıcının belirli bir eylemi gerçekleştirmek için veya belirli verileri okumak için uygun izne sahip olıp olmadığını belirleme sürecini ifade eder .</p> 

- <strong> Principle </strong> : <p> Şu anda kimliği doğrulanmış kullanıcıyı ifade eder. </p>

- <strong> Granted authority </strong> : <p> Kimliği doğrulanmış kullanım iznini ifade eder .</p>

- <Strong> Role </strong> : <p> Kimliği doğrulanmış kullanıcının bir gruo izinlerini ifade eder .
 
 ## JWT Nedir ? Neden ihtiyacımız var ?
 
 <p>JWT(JSON Web Tokens), bir RFC7519 endüstri standartıdır. JWT, kullanıcının doğrulanması, web servis güvenliği, bilgi güvenliği gibi birçok konuda kullanılabilir. JWT oldukça popüler ve tercih edilen bir yöntemdir. </p>
 
 <p> Web projelerimizi geliştirirken kullanıcı kimliklendirme/yetkilendirme işlemi oldukça önemlidir. Uygulamamızı yetkisiz kişilerden korumak ve yalnızca yetkili kullanıcıların erişimi için çeşitli yöntemler kullanırız. Bu çözümlerden birisi de token kullanmaktır. Mesela sunucu, kullanıcının yönetici ayrıcalıklarına sahip olduğunu belirten bir anahtar(token) oluşturabilir ve bunu kullanıcıya gönderebilir. Kullanıcı daha sonra bu anahtar ile kendisine tanımlanmış olan yönetici yetkisini bir istemci de kullanabilir. </p>
 --- 
 
 ### Json Web Token Yapısı
 
 <p> JWT, Base64 biçiminde kodlanmış 3 ayrı JSON parçasından oluşmaktadır. Parçalar nokta (.) sembolüyle ayrılmakta ve bir bütün olarak JWT’yi temsil etmektedir. Bunlar <strong> header </strong> , <strong> payload </strong> ve <strong> verify signature </strong> ’dan oluşmaktadır. </p>
 
 
 ![jwt3Bölüm](https://user-images.githubusercontent.com/74687192/124367766-d1d16980-dc62-11eb-807d-3bc57304fcd1.jpeg)
 
 - Header, JWT başlık bilgisi JSON biçiminde yazılmakta ve standart bazı alanları bulunmaktadır.
 - “alg” Veri bütünlüğünü korumak için kullanılacak cryptotic algoritmayı belirtir. İstediğiniz algoritmayı burada seçebilirsiniz.
 - “typ” ise JWT nesnesi olduğunu belirtir.
 - Payload, içinde kullanıcıya ait kimlik, zaman aşımı ve kullanıcı yetkileri gibi alanlar yer alabilir. Bunlara ek olarak eğer sistem üzerinde role tabanlı işlem yapılacaksa burada belirtilir.
 - Verify signature, imza kısmı token üreticisi ve tüketicisi arasındaki veri bütünlüğünü garanti etmektedir. İmza oluşturulurken JOSE başlığında tanımlı algoritma kullanılmaktadır.
 
### Avantajları 
- JWT’ler durumsuz (stateless) oldukları için kullanıcı durumlarının elde edilmesi için veritabanı sorgulamasına gerek kalmaz.
- Oturum yönetimi, çerezler kullanılmaksızın yapılabilir.
- Tek bir anahtar, birden fazla arka uçta (backend) kullanılabilir.
- Veritabanı sorgulaması ya da dosya sistemi kullanımı gerektirmediği için performanslıdır.
 
### Dezavantajları 
JWT anahtarı yeterince güçlü değilse kırılması daha kolay olur.

---
## Spring Security Mimarisine Genel Bakış
![jwtspringsecurity](https://user-images.githubusercontent.com/74687192/124367767-d3029680-dc62-11eb-9b4d-3dd1b0b144cc.png)

![Jwt](https://user-images.githubusercontent.com/74687192/124367768-d39b2d00-dc62-11eb-81e6-805d6fb27bc7.png)
 
 - <strong> AUTHENTICATON MANAGER </strong> : <p> AuthenticationManager'ı birden fazla sağlayıcıyı kaydedebileceğiniz bir koordinatör olarak düşünebilirsiniz ve istek türüne göre doğru sağlayıcıya bir kimlik doğrulama isteği gönderir. </p>
 
 - <strong> AUTHENTICATION PROVIDER </strong> : <p> AuthenticationProvider, belirli kimlik doğrulama türlerini işler. Arayüzü yalnızca iki fonksiyonu ortaya çıkarır <p>
 1.si <strong> authenticate </strong> : istekle kimlik doğrulaması gerçekleştirir.
 2.si <strong> supports </strong> : bu sağlayıcının belirtilen kimlik doğrulama türünü destekleyip desteklemediğini kontrol eder.
  
 - <strong> USERDETAILS SERVICE <strong> : <p> Spring dökümantasyonunda kullanıcıya özel verileri yükleyen bir çekirdek arabirim olarak tanımlanır </p>
 
  `loadUserByUserName` :  kullanıcı adını parametre olarak kabul eder ve kullanıcı kimliği nesnesini döndürür.
 
 ![image](https://user-images.githubusercontent.com/74687192/124368137-dc8dfd80-dc66-11eb-8b02-412222a5ff78.png)

 
 

 ## Spring Security ile JWT kullanarak Authentication
 
<p> Şimdi Spring Securityi Jwt belirteci ile durumsuz kimlik doğrulaması için yapılandıralım . Spring Securityi yapılandırmak için `@EnableWebSecurity` anatasyonuna ihtiyacımız vardır. Ayrıca yapılandırma sürecini basitleştirmek için framework `WebSecurityConfifurerAdapter` ortaya çıkarır . bağdaştırıcıyı genişletecek ve her iki işlevini de şu şekilde geçersiz kılacaktır: </p>
 
 - Doğru sağlayıcı ile kimlik doğrulama yöneticisini yapılandırma
 - Web güvenliğini yapılandırma (genel URL'ler, özel URL'ler, yetkilendirme vb.)
 
 ![image](https://user-images.githubusercontent.com/74687192/124368130-b49e9a00-dc66-11eb-81d0-332f66b51c23.png)
  
 Buradaki auth.userDetailsService UserDetailsService implementasyonumuzu kullanarak bir Daoprovider fonksiyon çağrısı başlatır.
 ![image](https://user-images.githubusercontent.com/74687192/124380189-104b4080-dcc4-11eb-82f6-6c7a1212822a.png)
 
 Password işlemlerinin şifreli bir biçimde kaydedilmesini istersek eğer Password Encoder metodunu eklememiz gerekmektedir 
 Biz burada <strong> bcrypt </strong> hash algoritmasını kullanacağız
 
 ![image](https://user-images.githubusercontent.com/74687192/124380234-57393600-dcc4-11eb-8680-8f2df140de56.png)

 <p> Kimlik Doğrulama yöneticisini yapılandırdıktan sonra şimdi web güvenliğini yapılandırmamız gerekiyor. Bir Rest Api implementasyonu uyguluyoruz ve JWT belirteci ile durum bilgisi olmayan kimlik doğrulamaya ihtiyacımız var . herefore, we need to set the following options:
  
- CORS'u etkinleştirin ve CSRF'yi devre dışı bırakın
- Oturum yönetimini stateless olarak ayarlayın.
- Yetkisiz istekler hata işleyicisini ayarla
- Jwt token filteri ekleyin
- Endpoinstlerde izinleri ayarlayın
  
  Bu Yapılandırma Aşağıdaki gibi uygulanır.
  ![image](https://user-images.githubusercontent.com/74687192/124380600-32de5900-dcc6-11eb-8811-01da6c4deb1c.png)
  ![image](https://user-images.githubusercontent.com/74687192/124380607-3c67c100-dcc6-11eb-8f00-604b87771565.png)
  ![image](https://user-images.githubusercontent.com/74687192/124380632-5dc8ad00-dcc6-11eb-87d5-fbf7b1d21ea6.png)

 <p> Oturum açma API işlevimizi uygulamadan önce bir adımla daha ilgilenmemiz gerekiyor - kimlik doğrulama yöneticisine erişmemiz gerekiyor. Varsayılan olarak, herkese açık değildir ve onu yapılandırma sınıfımızda açıkça bir fasulye olarak göstermemiz gerekir.</p>
 ![image](https://user-images.githubusercontent.com/74687192/124380652-7fc22f80-dcc6-11eb-850b-a9a91ff8f866.png)
 
 Son olarak Bu şekilde tamamladık
 ![image](https://user-images.githubusercontent.com/74687192/124380670-9a94a400-dcc6-11eb-919d-0c8ba4d4b575.png)

## Spring Securityde Authorization için 2 yol vardir .
 
 Önceki Bölümde bir kimlik doğrulama süreci oluşturduk ve geneş/özel URK leri yapılandırdık . Bu basit uygulamalar için yeterli olabilir ancak çoğu zaman real uygulamalarda kullanıcılarımız için rol tabanlı erişim politikalarına ihtiyacımız var . Bu Bölümde Spring Security kullanarak rol tabanlı bir yetkilendirme şeması oluşturacağız .
 
  `Spring Security çerçevesi, yetkilendirme şemasını kurmak için bize iki seçenek sunar` 
- Url Tabankı yapılandırma
- Anatasyon tabanlı yapılandırma
 
 İlk Başta url tabanlı yapılandırmanın nasıl gözüktüğüne bakalım 
 ![image](https://user-images.githubusercontent.com/74687192/124380783-62da2c00-dcc7-11eb-80c6-3f88bcf8a814.png)
  Basit ve anlaşılır gözüküyor fakat bir dezavantajı var : proje büyüdükçe bu daha karmaşık bir hal alacaktır. Bu nedenle anatasyon tabanlı yapılandırmayı seçmek uygulamanın sağlığı açısından daha sağlıklı olacaktır .
 
 Spring Security , web güvenliği için aşağıdaki ek açıklamaları tanımlar
- @PreAuthorize
- @PostAuthorize
- @PreFilter
- @PostFilter
- @Secured
- @RolesAllowed
 Bu anatasyonlar default olarak devre dışıdır biz bunları şu şekilde aktif edeceğiz .

 ![image](https://user-images.githubusercontent.com/74687192/124380884-04fa1400-dcc8-11eb-9d47-02aab832b8de.png)
 
 Etkinleştirdikten sonra Apilerimizin endpointlerinde rol tabanlı erişim altyapısını kullanabiliriz .

 ![image](https://user-images.githubusercontent.com/74687192/124380918-3bd02a00-dcc8-11eb-87e7-d276a75112f2.png)

 
 ### Role Name Default Prefix
 Spring Security iki terimi birbirinden ayırır:
 
 - Authority bireysel bir izni temsil eder
 - Role : bir grup izinleri temsil eder. 
 Örnek olarak :
 
 Authority: @PreAuthorize(“hasAuthority(‘EDIT_BOOK’)”)
Role: @PreAuthorize(“hasRole(‘BOOK_ADMIN’)”)
 Bunu kafa karıştırıcı buluyorsak şu şekilde kapatabiliriz
 
![image](https://user-images.githubusercontent.com/74687192/124380973-b5681800-dcc8-11eb-979a-f31d60e83325.png)
 
 
 
 # Output
 ![Ekran Alıntısı](https://user-images.githubusercontent.com/74687192/124387911-00922300-dce9-11eb-8245-75f2a7d6e1dd.PNG)
![MerhabaJwt](https://user-images.githubusercontent.com/74687192/124387912-012ab980-dce9-11eb-8cd1-e52f8f85c255.PNG)
