<#import "blocks/template.ftlh" as t>
<@t.template user>
    <div id="myCarousel" class="carousel slide" data-ride="carousel">
        <ol class="carousel-indicators">
            <#list images as image>
                <li data-target="#myCarousel" data-slide-to="0" class=""></li>
            </#list>
        </ol>
        <div class="carousel-inner">
            <#list images as image>
                <div class="carousel-item <#if image.previewImage>active</#if>">
                    <svg class="bd-placeholder-img" width="100%" height="100%" xmlns="http://www.w3.org/2000/svg"
                         role="img" aria-label=" :  " preserveAspectRatio="xMidYMid slice" focusable="false">
                        <title></title>
                        <rect width="100%" height="100%" fill="#777"></rect>
                        <text x="50%" y="50%" fill="#777" dy=".3em"></text>
                    </svg>

                    <div class="container">
                        <div class="carousel-caption">
                            <a href="/images/${image.id}">
                                <img class="product" style="height: 100px" src="/images/${image.id}">
                            </a>
                        </div>
                    </div>
                </div>
            </#list>

        </div>
        <a class="carousel-control-prev" href="#myCarousel" role="button" data-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
        </a>
        <a class="carousel-control-next" href="#myCarousel" role="button" data-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
        </a>
    </div>
    <hr>
    <p><h1 style="text-align: center">${product.title} | ${product.price} ₽</h1></p>
    <p style="text-align: center">${product.description}</p>
    <p style="text-align: center">Марка: ${product.brand.name}</p>
    <p style="text-align: center">Производитель: ${product.manufacturer.name}</p>
    <hr>

    <#if user.isAdmin()>
        <form action="/admin/update-product/${product.id}" method="post">
            <label for="brand">Выберите марку:</label>
            <select class="form-select form-select-lg mb-3" aria-label="Large select example" id="brand" name="brand">
                <#list brands as brand>
                    <option value="${brand.id}" <#if brand.id == product.brand.id>selected</#if>>${brand.name}</option>
                </#list>
            </select>
            <br>
            <label for="manufacturer">Выберите производителя:</label>
            <select id="manufacturer" name="manufacturer">
                <#list manufacturers as manufacturer>
                    <option value="${manufacturer.id}" <#if manufacturer.id == product.manufacturer.id>selected</#if>>${manufacturer.name}</option>
                </#list>
            </select>
            <br>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <input class="btn btn-dark"  type="submit" value="Сохранить">
        </form>
        <hr>
    </#if>
    <form action="/product/${id}/review" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <div>
            <label for="rating">Оценка:</label>
            <select id="rating" name="rating">
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
            </select>
        </div>
        <div>
            <label for="comment">Отзыв:</label>
            <textarea id="comment" name="comment" rows="2" cols="40"></textarea>
        </div>
        <div class="mb-3">
            <label for="photo">Фотография:</label>
            <input type="file" id="photo" name="photo">
        </div>
        <button class="btn btn-dark" type="submit">Добавить отзыв</button>
    </form>
    <hr>
    <div>
        <h2>Отзывы:</h2>
        <ul class="list-group mb-5">
            <#list product.reviews as review>
                <li class="list-group-item">
                    Оценка: ${review.rating} <br>Отзыв: ${review.comment} <br>Пользователь: ${review.user.username}<br>
                    <#--<#if review.photo??>
                        <img src="data:${review.photo.contentType};base64,${review.photo}" alt="Photo">
                    </#if>-->
                    <#if user.isAdmin()>
                        <form action="/product/${id}/review/delete/${review.id}" method="post">
                            <input type="hidden" name="_csrf" value="${_csrf.token}">
                            <button type="submit" class="btn btn-danger m-3">
                                Удалить отзыв
                            </button>
                        </form>
                    </#if>

                </li>
            </#list>
        </ul>
    </div>
</@t.template>
