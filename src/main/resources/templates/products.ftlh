<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Поиск товаров</h1><hr>

    <div class="grid text-center">
        <a style="width: 35%" class="btn btn-outline-success mb-3" href="/products/top-brands-best">Лучшие марки</a><br>
        <a style="width: 35%" class="btn btn-outline-danger mb-3" href="/products/top-brands-worst">Худшие марки</a><br>
        <a style="width: 35%" class="btn btn-outline-success mb-3" href="/products/top-manufacturers-best">Лучшие производители</a><br>
        <a style="width: 35%" class="btn btn btn-outline-danger" href="/products/top-manufacturers-worst">Худшие производители</a><br>
    </div>

    <hr>
    <form method="get" action="/">
        <div class="form-row">
            <div class="form-group col-md-10">
                <label for="searchWord">Поиск по объявлениям</label>
                <input type="text" name="searchWord" <#if searchWord??>value="${searchWord}"</#if> class="form-control"
                       id="searchWord">
            </div>
            <button style="width: 100%" type="submit" class="btn btn-dark">Поиск</button>
        </div>
    </form>
    <hr>
    <div class="card-columns">
        <#list products as product>
            <a href="/product/${product.id}" class="product-link">
                <div class="card text-white bg-dark my-3 product">
                    <img src="/images/${product.previewImageId}"
                         class="card-img-top">
                    <div class="card-body">
                        <h5 class="card-title">${product.title}</h5>
                        <p class="card-text">${product.price} ₽</p>
                    </div>
                </div>
            </a>
        <#else>
            <h1 style="text-align: center">Не найдено</h1>
        </#list>
    </div>
</@t.template>